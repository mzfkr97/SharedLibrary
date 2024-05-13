Pod::Spec.new do |s|
    s.name                     = 'shared'
    s.version                  = '1.0.5'
    s.homepage                 = 'https://github.servicechannel.com/ServiceChannel/sdk-android'
s.source       = { :http => "https://github.com/mzfkr97/SharedLibrary/releases/download/1.0.5/shared.zip" }
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'https://opensource.org/licenses/Apache-2.0'
    s.summary                  = 'ServiceChannel mobile team'
    s.vendored_frameworks      = "shared.xcframework"
    s.requires_arc         = true
    s.ios.deployment_target = '11.0'
end
