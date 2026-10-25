param(
    [string]$Message = "Automated AaloPro source sync",
    [switch]$OnlyStaged
)

$ErrorActionPreference = "Stop"
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
Set-Location $repoRoot

$expectedRemote = "https://github.com/riyadhshajib/aalopro-bangla-primary-curriculum.git"
$remote = (git remote get-url origin 2>$null).Trim()
if ($remote -ne $expectedRemote) {
    throw "Refusing to sync an unexpected remote: $remote"
}

if (-not $OnlyStaged) { git add -A }
$staged = @(git diff --cached --name-only)
if ($staged.Count -eq 0) {
    Write-Output "No source changes to sync."
    exit 0
}

$blockedNamePattern = '(^|/)(local\.properties|\.env($|\.)|.*\.(jks|keystore|p12|pem|key|apk|aab)$|release-signing-credentials\.txt|release-signing-credential\.clixml)'
$blockedNames = @($staged | Where-Object { $_ -match $blockedNamePattern -and $_ -notmatch '(^|/)\.env\.example$' })
if ($blockedNames.Count -gt 0) {
    throw "Refusing to sync sensitive or generated files: $($blockedNames -join ', ')"
}

$diff = git diff --cached --no-ext-diff -- .
$secretPattern = '(BEGIN (RSA |EC |OPENSSH )?PRIVATE KEY|sk-[A-Za-z0-9_-]{20,}|ghp_[A-Za-z0-9]{20,}|github_pat_[A-Za-z0-9_]{20,}|AKIA[0-9A-Z]{16})'
if (($diff | Out-String) -match $secretPattern) {
    throw "Potential credential detected in staged source. Sync stopped."
}

git diff --cached --check
if ($LASTEXITCODE -ne 0) {
    throw "Git whitespace validation failed."
}

$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm"
git commit -m "$Message ($timestamp)"
if ($LASTEXITCODE -ne 0) {
    throw "Commit failed."
}

git push origin main
if ($LASTEXITCODE -ne 0) {
    throw "Push failed."
}

Write-Output "AaloPro source synchronized successfully."
