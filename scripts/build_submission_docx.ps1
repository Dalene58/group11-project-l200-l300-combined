$mdPath = "SUBMISSION_REPORT.md"
$docxPath = "SUBMISSION_REPORT.docx"
$tmpRoot = Join-Path $PWD ".tmp_docx"
$zipPath = "SUBMISSION_REPORT.zip"

if (Test-Path $tmpRoot) { Remove-Item $tmpRoot -Recurse -Force }
New-Item -ItemType Directory -Path $tmpRoot | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tmpRoot "_rels") | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tmpRoot "word") | Out-Null

$raw = Get-Content $mdPath -Raw
$lines = $raw -split "`r?`n"

function Escape-XmlText([string]$text) {
    $text = $text.Replace('&','&amp;')
    $text = $text.Replace('<','&lt;')
    $text = $text.Replace('>','&gt;')
    $text = $text.Replace('"','&quot;')
    return $text
}

$paragraphs = New-Object System.Collections.Generic.List[string]
foreach ($line in $lines) {
    if ([string]::IsNullOrWhiteSpace($line)) {
        $paragraphs.Add("<w:p/>")
    } else {
        $safe = Escape-XmlText $line
        $paragraphs.Add("<w:p><w:r><w:t xml:space=`"preserve`">$safe</w:t></w:r></w:p>")
    }
}

$documentXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" xmlns:w10="urn:schemas-microsoft-com:office:word" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 wp14">
  <w:body>
    $($paragraphs -join "`n    ")
    <w:sectPr>
      <w:pgSz w:w="12240" w:h="15840"/>
      <w:pgMar w:top="1440" w:right="1440" w:bottom="1440" w:left="1440" w:header="708" w:footer="708" w:gutter="0"/>
      <w:cols w:space="708"/>
      <w:docGrid w:linePitch="360"/>
    </w:sectPr>
  </w:body>
</w:document>
"@

$contentTypes = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>
"@

$rels = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>
"@

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText((Join-Path $tmpRoot "[Content_Types].xml"), $contentTypes, $utf8NoBom)
[System.IO.File]::WriteAllText((Join-Path $tmpRoot "_rels/.rels"), $rels, $utf8NoBom)
[System.IO.File]::WriteAllText((Join-Path $tmpRoot "word/document.xml"), $documentXml, $utf8NoBom)

if (Test-Path $zipPath) { Remove-Item $zipPath -Force }
if (Test-Path $docxPath) { Remove-Item $docxPath -Force }
[System.IO.Compression.ZipFile]::CreateFromDirectory($tmpRoot, $zipPath)
Rename-Item -Path $zipPath -NewName $docxPath -Force
Remove-Item $tmpRoot -Recurse -Force
Write-Output "Created $docxPath"