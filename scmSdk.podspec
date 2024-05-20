Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.28'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :git => 'https://github.com/mzfkr97/SharedLibrary.git', :tag => spec.version }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Mobile team'
    spec.ios.vendored_frameworks = 'ios-arm64/scmSdk.framework'
    spec.libraries                = 'c++'
    spec.static_framework         = false
    spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
    spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
    spec.ios.deployment_target = '11.0'
end
