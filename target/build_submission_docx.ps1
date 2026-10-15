$root = Resolve-Path .
$mdPath = Join-Path $root "SUBMISSION_REPORT.md"
$docxPath = Join-Path $root "submission_report.docx"
$tmp = Join-Path $root "target/docx_tmp"
$zipPath = Join-Path $root "target/SUBMISSION_REPORT.zip"

if (Test-Path $tmp) { Remove-Item $tmp -Recurse -Force }
if (Test-Path $zipPath) { Remove-Item $zipPath -Force }
New-Item -ItemType Directory -Path $tmp | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tmp "_rels") | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tmp "word") | Out-Null

function Escape-Xml([string]$text) {
    return $text.Replace('&','&amp;').Replace('<','&lt;').Replace('>','&gt;').Replace('"','&quot;').Replace("'",'&apos;')
}

function Clean-Markdown([string]$text) {
    $clean = $text -replace '`',''
    $clean = $clean -replace '\$',''
    $clean = $clean -replace '\*\*',''
    $clean = $clean -replace '(?<!\w)\*(?!\w)',''
    return $clean.Trim()
}

function New-Run([string]$text, [int]$size = 23, [bool]$bold = $false) {
    $escaped = Escape-Xml (Clean-Markdown $text)
    $boldXml = if ($bold) { '<w:b/>' } else { '' }
    return "<w:r><w:rPr>$boldXml<w:sz w:val='$size'/></w:rPr><w:t xml:space='preserve'>$escaped</w:t></w:r>"
}

function New-Table([string[]]$rows) {
    $tableXml = "<w:tbl><w:tblPr><w:tblW w:w='0' w:type='auto'/><w:tblBorders><w:top w:val='single' w:sz='6' w:color='B7C4D6'/><w:left w:val='single' w:sz='6' w:color='B7C4D6'/><w:bottom w:val='single' w:sz='6' w:color='B7C4D6'/><w:right w:val='single' w:sz='6' w:color='B7C4D6'/><w:insideH w:val='single' w:sz='4' w:color='D9E1EC'/><w:insideV w:val='single' w:sz='4' w:color='D9E1EC'/></w:tblBorders><w:tblCellMar><w:top w:w='90' w:type='dxa'/><w:left w:w='110' w:type='dxa'/><w:bottom w:w='90' w:type='dxa'/><w:right w:w='110' w:type='dxa'/></w:tblCellMar></w:tblPr>"
    $rowIndex = 0
    foreach ($row in $rows) {
        $cells = $row.Trim().Trim('|').Split('|') | ForEach-Object { $_.Trim() }
        $tableXml += '<w:tr>'
        foreach ($cell in $cells) {
            $isHeader = $rowIndex -eq 0
            $shading = if ($isHeader) { "<w:shd w:fill='1F4E79'/><w:color w:val='FFFFFF'/>" } else { '' }
            $tableXml += "<w:tc><w:tcPr><w:tcW w:w='3000' w:type='dxa'/>$shading</w:tcPr><w:p><w:pPr><w:spacing w:before='20' w:after='20'/></w:pPr>$(New-Run $cell 20 $isHeader)</w:p></w:tc>"
        }
        $tableXml += '</w:tr>'
        $rowIndex++
    }
    return $tableXml + '</w:tbl>'
}

$paras = New-Object System.Collections.Generic.List[string]
$lines = Get-Content $mdPath
$inCode = $false
$index = 0

while ($index -lt $lines.Count) {
    $trim = $lines[$index].TrimEnd()

    if ($trim -like '```*') {
        $inCode = -not $inCode
        $index++
        continue
    }

    if ([string]::IsNullOrWhiteSpace($trim)) {
        $paras.Add('<w:p/>')
        $index++
        continue
    }

    if (-not $inCode -and $trim -match '^\|') {
        $tableRows = New-Object System.Collections.Generic.List[string]
        while ($index -lt $lines.Count -and $lines[$index].Trim() -match '^\|') {
            $row = $lines[$index].Trim()
            if ($row -notmatch '^\|\s*:?-+:?\s*(\|\s*:?-+:?\s*)+\|?$') { $tableRows.Add($row) }
            $index++
        }
        if ($tableRows.Count -gt 0) { $paras.Add((New-Table $tableRows.ToArray())) }
        continue
    }

    $style = ''
    $text = $trim

    if ($inCode) {
        $style = 'CodeStyle'
    } elseif ($trim -match '^#\s+') {
        $style = 'Title'
        $text = $trim -replace '^#\s+', ''
    } elseif ($trim -match '^##\s+') {
        $style = 'Heading1'
        $text = $trim -replace '^##\s+', ''
    } elseif ($trim -match '^###\s+') {
        $style = 'Heading2'
        $text = $trim -replace '^###\s+', ''
    } elseif ($trim -match '^\d+\.\s+') {
        $style = 'Body'
        $text = $trim
    } elseif ($trim -match '^-\s+') {
        $style = 'Body'
        $text = '• ' + ($trim -replace '^-\s+', '')
    } elseif ($trim -match '^\|') {
        $style = 'Body'
        $text = ($trim -replace '\|', '  |  ')
    } else {
        $style = 'Body'
    }

    $escaped = Escape-Xml (Clean-Markdown $text)

    switch ($style) {
        'Title' {
            $paras.Add("<w:p><w:pPr><w:pStyle w:val='Title'/><w:spacing w:before='120' w:after='220'/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val='38'/></w:rPr><w:t xml:space='preserve'>$escaped</w:t></w:r></w:p>")
        }
        'Heading1' {
            $paras.Add("<w:p><w:pPr><w:pStyle w:val='Heading1'/><w:spacing w:before='220' w:after='120'/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val='30'/></w:rPr><w:t xml:space='preserve'>$escaped</w:t></w:r></w:p>")
        }
        'Heading2' {
            $paras.Add("<w:p><w:pPr><w:pStyle w:val='Heading2'/><w:spacing w:before='160' w:after='90'/></w:pPr><w:r><w:rPr><w:b/><w:sz w:val='26'/></w:rPr><w:t xml:space='preserve'>$escaped</w:t></w:r></w:p>")
        }
        'CodeStyle' {
            $paras.Add("<w:p><w:pPr><w:spacing w:before='40' w:after='40'/></w:pPr><w:r><w:rPr><w:rFonts w:ascii='Consolas' w:hAnsi='Consolas'/><w:sz w:val='20'/></w:rPr><w:t xml:space='preserve'>$escaped</w:t></w:r></w:p>")
        }
        default {
            $paras.Add("<w:p><w:pPr><w:spacing w:before='40' w:after='70'/></w:pPr><w:r><w:rPr><w:sz w:val='23'/></w:rPr><w:t xml:space='preserve'>$escaped</w:t></w:r></w:p>")
        }
    }
    $index++
}

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

$document = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
 xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
 xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
 xmlns:v="urn:schemas-microsoft-com:vml"
 xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
 xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
 xmlns:w10="urn:schemas-microsoft-com:office:word"
 xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
 xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
 xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
 xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
 xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
 xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
 xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
 mc:Ignorable="w14 w15 wp14">
  <w:body>
    $(($paras -join "`n    "))
    <w:sectPr>
      <w:pgSz w:w="12240" w:h="15840"/>
      <w:pgMar w:top="1440" w:right="1200" w:bottom="1440" w:left="1200" w:header="708" w:footer="708" w:gutter="0"/>
      <w:cols w:space="708"/>
      <w:docGrid w:linePitch="360"/>
    </w:sectPr>
  </w:body>
</w:document>
"@

[System.IO.File]::WriteAllText((Join-Path $tmp "[Content_Types].xml"), $contentTypes, (New-Object System.Text.UTF8Encoding($false)))
[System.IO.File]::WriteAllText((Join-Path $tmp "_rels/.rels"), $rels, (New-Object System.Text.UTF8Encoding($false)))
[System.IO.File]::WriteAllText((Join-Path $tmp "word/document.xml"), $document, (New-Object System.Text.UTF8Encoding($false)))

Compress-Archive -Path (Join-Path $tmp '*') -DestinationPath $zipPath -Force
if (Test-Path $docxPath) { Remove-Item $docxPath -Force }
Move-Item $zipPath $docxPath
Remove-Item $tmp -Recurse -Force

Write-Output "Created $docxPath"