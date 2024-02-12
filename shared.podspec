Pod::Spec.new do |spec|
    spec.name                     = 'shared'
    spec.version       = "1.0.6"
    spec.homepage                 = 'https://github.com/mzfkr97/SharedLibrary'
    spec.source       = { :git => 'https://github.com/mzfkr97/SharedLibrary.git', :branch => '1_0_5_ver' }
    spec.license                  = 'https://opensource.org/licenses/Apache-2.0'
    spec.summary                  = 'SharedLibraryZhurid'

    spec.vendored_frameworks      = "shared.xcframework"

    spec.ios.deployment_target = '11.0'

    spec.user_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=*simulator*]' => 'arm64' }
    spec.pod_target_xcconfig = { 'EXCLUDED_ARCHS[sdk=*simulator*]' => 'arm64' }

end
