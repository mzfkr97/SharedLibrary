Pod::Spec.new do |s|
    s.name                     = 'shared'
    s.version                  = '1.0.6'
    s.homepage                 = 'https://github.servicechannel.com/ServiceChannel/sdk-android'
    s.source       = { :git => "https://github.com/mzfkr97/SharedLibrary.git", :tag => s.version.to_s}
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'https://opensource.org/licenses/Apache-2.0'
    s.summary                  = 'ServiceChannel mobile team'
    s.vendored_frameworks      = "scmSdk.framework"
    s.requires_arc         = true
s.libraries = 'stdc++','z','sqlite3','c++'
s.platform = :ios
    s.ios.deployment_target = '11.0'
end
