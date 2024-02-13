Pod::Spec.new do |spec|
    spec.name                     = 'shared'
    spec.version       = "1.0.7"
    spec.homepage                 = 'https://github.com/mzfkr97/SharedLibrary'
    spec.source       = { :http => "https://github.com/mzfkr97/SharedLibrary/tree/shared.xcframework.zip" }
    spec.license                  = 'https://opensource.org/licenses/Apache-2.0'
    spec.summary                  = 'SharedLibraryZhurid'

    spec.vendored_frameworks      = "shared.xcframework"

    spec.ios.deployment_target = '11.0'

    spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=*simulator*]' => 'arm64' }
    spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=*simulator*]' => 'arm64' }

end
