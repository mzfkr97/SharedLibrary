Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.50'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :git => 'https://github.com/mzfkr97/SharedLibrary.git', :tag => '7.7.50' }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Mobile team'
    spec.ios.vendored_frameworks = 'SharedLibrary-7.7.30/scmSdk.framework'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
end
