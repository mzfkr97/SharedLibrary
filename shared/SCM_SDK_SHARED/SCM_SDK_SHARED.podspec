Pod::Spec.new do |s|
    s.name                     = 'scm-sdk'
    s.version                  = '7.7.44'
    s.homepage                 = 'https://github.servicechannel.com/ServiceChannel/sdk-android'
    s.source       = { :git => "https://github.servicechannel.com/ServiceChannel/sdk-android.git", :branch => 'MSDK_ios_publishing_test_podspec'}
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'https://opensource.org/licenses/Apache-2.0'
    s.summary                  = 'ServiceChannel mobile team'
    s.vendored_frameworks      = "scmSdk.xcframework"
    s.requires_arc         = true
    s.ios.deployment_target = '11.0'
end
