param([Parameter(Mandatory=$true)][string]$Bundle)
$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip=[IO.Compression.ZipFile]::OpenRead((Resolve-Path $Bundle).Path)
$results=@();$experimental=@()
try {
 foreach($e in $zip.Entries){
  if($e.FullName -match '(encoder\.onnx|tokenizer\.onnx|needle3\.cact|\.gguf$|libonnxruntime)'){$experimental += $e.FullName}
  if($e.FullName -notmatch '/lib/arm64-v8a/.*\.so$'){continue}
  $stream=$e.Open();$ms=[IO.MemoryStream]::new()
  try{$stream.CopyTo($ms);$b=$ms.ToArray()}finally{$stream.Dispose();$ms.Dispose()}
  if($b.Length -lt 64 -or $b[0] -ne 127 -or $b[4] -ne 2 -or $b[5] -ne 1){throw "Unsupported ELF: $($e.FullName)"}
  $offset=[BitConverter]::ToUInt64($b,32);$size=[BitConverter]::ToUInt16($b,54);$count=[BitConverter]::ToUInt16($b,56)
  $alignments=@();$ok=$true
  for($i=0;$i -lt $count;$i++){
   $p=[int]($offset+$i*$size)
   if($p+56 -gt $b.Length){throw 'Invalid ELF program header'}
   if([BitConverter]::ToUInt32($b,$p) -eq 1){
    $align=[BitConverter]::ToUInt64($b,$p+48);$alignments += $align
    $fileOffset=[BitConverter]::ToUInt64($b,$p+8);$virtual=[BitConverter]::ToUInt64($b,$p+16)
    if($align -lt 16384 -or ($fileOffset % 16384) -ne ($virtual % 16384)){$ok=$false}
   }
  }
  $results += [PSCustomObject]@{library=$e.FullName;loadAlignments=$alignments;passed=$ok -and $alignments.Count -gt 0}
 }
}finally{$zip.Dispose()}
$report=[ordered]@{elf64=$results;experimentalAssets=$experimental;passed=(@($results | Where-Object {-not $_.passed}).Count -eq 0 -and $experimental.Count -eq 0);runtime16kTested=$false}
$report | ConvertTo-Json -Depth 6
if(-not $report.passed){exit 1}
