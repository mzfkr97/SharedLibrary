Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.28'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :git => 'https://github.com/mzfkr97/SharedLibrary.git', :tag => '7.7.28' }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Mobile team'
    spec.ios.vendored_frameworks = 'ios-arm64/scmSdk.framework'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
end
