param(
    [string]$ElasticsearchUrl = "http://127.0.0.1:9200",
    [string]$KibanaStatusUrl = "http://127.0.0.1:5601/api/status",
    [string]$AppHealthUrl = "http://127.0.0.1:8080/health",
    [string]$RedisHost = "127.0.0.1",
    [int]$RedisPort = 6379,
    [string]$FilebeatProcessName = "filebeat"
)

function Test-Http {
    param(
        [string]$Name,
        [string]$Url
    )
    try {
        $resp = Invoke-WebRequest -Uri $Url -TimeoutSec 5 -UseBasicParsing
        if ($resp.StatusCode -ge 200 -and $resp.StatusCode -lt 400) {
            return @{ Name = $Name; Ok = $true; Detail = "$($resp.StatusCode) $Url" }
        }
        return @{ Name = $Name; Ok = $false; Detail = "$($resp.StatusCode) $Url" }
    } catch {
        return @{ Name = $Name; Ok = $false; Detail = $_.Exception.Message }
    }
}

function Test-Tcp {
    param(
        [string]$Name,
        [string]$Host,
        [int]$Port
    )
    try {
        $client = New-Object System.Net.Sockets.TcpClient
        $iar = $client.BeginConnect($Host, $Port, $null, $null)
        $ok = $iar.AsyncWaitHandle.WaitOne(3000, $false)
        if (-not $ok) {
            $client.Close()
            return @{ Name = $Name; Ok = $false; Detail = "Connect timeout: $Host`:$Port" }
        }
        $client.EndConnect($iar)
        $client.Close()
        return @{ Name = $Name; Ok = $true; Detail = "TCP OK $Host`:$Port" }
    } catch {
        return @{ Name = $Name; Ok = $false; Detail = $_.Exception.Message }
    }
}

function Test-ProcessRunning {
    param(
        [string]$Name,
        [string]$ProcessName
    )
    try {
        $proc = Get-Process -Name $ProcessName -ErrorAction SilentlyContinue
        if ($null -ne $proc) {
            return @{ Name = $Name; Ok = $true; Detail = "Found process: $ProcessName" }
        }
        return @{ Name = $Name; Ok = $false; Detail = "Process not found: $ProcessName" }
    } catch {
        return @{ Name = $Name; Ok = $false; Detail = $_.Exception.Message }
    }
}

$results = @()
$results += Test-Http -Name "Elasticsearch HTTP" -Url $ElasticsearchUrl
$results += Test-Http -Name "Kibana Status API" -Url $KibanaStatusUrl
$results += Test-Http -Name "SpringBoot Health" -Url $AppHealthUrl
$results += Test-Tcp -Name "Redis TCP" -Host $RedisHost -Port $RedisPort
$results += Test-ProcessRunning -Name "Filebeat Process" -ProcessName $FilebeatProcessName

Write-Host "==== 本地可观测一键自检 ===="
$failCount = 0
foreach ($r in $results) {
    if ($r.Ok) {
        Write-Host ("[OK]   {0} -> {1}" -f $r.Name, $r.Detail) -ForegroundColor Green
    } else {
        Write-Host ("[FAIL] {0} -> {1}" -f $r.Name, $r.Detail) -ForegroundColor Red
        $failCount++
    }
}

if ($failCount -eq 0) {
    Write-Host "结论：全部通过，可进入联调/日志排障。" -ForegroundColor Green
    exit 0
}

Write-Host "结论：存在 $failCount 项失败，请先修复后再继续。" -ForegroundColor Yellow
exit 1
