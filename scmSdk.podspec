Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.51'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :http => 'https://github.com/mzfkr97/SharedLibrary/releases/download/7.7.51/scmSdk.framework.zip' }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
spec.vendored_frameworks      = "scmSdk.xcframework"
    spec.summary                  = 'Mobile team'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
end
