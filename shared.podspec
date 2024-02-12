Pod::Spec.new do |spec|
    spec.name                     = 'sharedLibraryZhurid'
    spec.version       = "1.0.3"
    spec.homepage                 = 'https://github.com/mzfkr97/SharedLibrary'
    spec.source       = { :http => "https://github.com/mzfkr97/SharedLibrary/releases/download/1.0.3/shared.xcframework.zip"}
    spec.authors                  = 'mzfkr97@gmail.com'
    spec.license                  = 'https://opensource.org/licenses/Apache-2.0'
    spec.summary                  = 'SharedLibraryZhurid'

    spec.vendored_frameworks      = "shared.xcframework"

    spec.ios.deployment_target = '11.0'

    spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=*simulator*]' => 'arm64' }
    spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=*simulator*]' => 'arm64' }

end
