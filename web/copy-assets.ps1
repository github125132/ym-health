# 从旧项目复制静态资源到前端 public 目录
$legacy = Join-Path $PSScriptRoot "..\legacy"
$public = Join-Path $PSScriptRoot "public"
$dirs = @('img','images','css','js','upload','fonts','ffont','newfont','alifont','erweicode')

Write-Host "Copying static assets from legacy project..."
foreach ($d in $dirs) {
    $src = Join-Path $legacy $d
    $dst = Join-Path $public $d
    if (Test-Path $src) {
        Copy-Item -Path $src -Destination $dst -Recurse -Force
        Write-Host "  $d - done"
    }
}
Write-Host "Done. Assets copied to web/public/"
