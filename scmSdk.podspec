Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = 'testFrame'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :http => 'https://github.com/mzfkr97/SharedLibrary/releases/download/7.7.30/scmSdk.xcframework.zip' }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Mobile team'
    spec.ios.vendored_frameworks = 'scmSdk.framework'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
end
