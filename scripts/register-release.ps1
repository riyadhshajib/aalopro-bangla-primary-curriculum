param(
  [Parameter(Mandatory = $true)][string]$Artifact,
  [Parameter(Mandatory = $true)][ValidateSet('dev','test','demo','production')][string]$Channel,
  [Parameter(Mandatory = $true)][string]$Version,
  [string]$Verification = 'Build produced; full verification not supplied',
  [string]$Limitations = 'See current audit report'
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$artifactPath = (Resolve-Path -LiteralPath $Artifact).Path
$extension = [IO.Path]::GetExtension($artifactPath).ToLowerInvariant()
if ($extension -notin @('.apk', '.aab')) { throw 'Artifact must be an APK or AAB.' }

$commit = (& git -C $projectRoot rev-parse --verify HEAD).Trim()
if ($LASTEXITCODE -ne 0 -or -not $commit) { throw 'Register only from a Git commit.' }
$shortCommit = $commit.Substring(0, 8)
$date = Get-Date -Format 'yyyyMMdd'
$releaseDirectory = Join-Path $projectRoot 'releases'
New-Item -ItemType Directory -Force -Path $releaseDirectory | Out-Null
$fileName = "AaloPro-$Version-$Channel-$date-$shortCommit$extension"
$destination = Join-Path $releaseDirectory $fileName
Copy-Item -LiteralPath $artifactPath -Destination $destination -Force
$hash = (Get-FileHash -Algorithm SHA256 -LiteralPath $destination).Hash.ToLowerInvariant()
$sizeMb = [math]::Round((Get-Item -LiteralPath $destination).Length / 1MB, 2)

$registry = Join-Path $projectRoot 'docs\RELEASE_REGISTRY.md'
$safeVerification = $Verification.Replace('|', '\|').Replace("`r", ' ').Replace("`n", ' ')
$safeLimitations = $Limitations.Replace('|', '\|').Replace("`r", ' ').Replace("`n", ' ')
$row = "| $Version | $Channel | $(Get-Date -Format 'yyyy-MM-dd') | ``$shortCommit`` | ``$fileName`` ($sizeMb MB) | ``$hash`` | $safeVerification | $safeLimitations |"
Add-Content -LiteralPath $registry -Value $row -Encoding UTF8

[pscustomobject]@{
  Artifact = $destination
  Version = $Version
  Channel = $Channel
  Commit = $commit
  Sha256 = $hash
  SizeMB = $sizeMb
} | Format-List
