Pod::Spec.new do |spec|
    spec.name                     = 'shared.'
    spec.version                  = '7.7.26'
    spec.homepage                 = 'https://github.com/mzfkr97/SharedLibrary.git'
    spec.source       = { :git => "https://github.com/mzfkr97/SharedLibrary", :tag => spec.version.to_s }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = 'MIT'
    spec.summary                  = 'Service channel mobile team'
    spec.vendored_frameworks      = "scmSdk.xcframework"
    spec.libraries                = "c++"
    spec.static_framework         = true
    spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
    spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64' }
    spec.ios.deployment_target = '11.0'
end
