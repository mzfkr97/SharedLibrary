Pod::Spec.new do |s|
    s.name                     = 'shared'
    s.version                  = '1.0.6'
    s.homepage                 = 'https://github.com/mzfkr97/SharedLibrary.git'
    s.source       = { :git => "https://github.com/mzfkr97/SharedLibrary.git", :tag => s.version.to_s}
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'MIT'
    s.summary                  = 'ServiceChannel mobile team'
    s.vendored_frameworks      = "podname/Frameworks/scmSdk.framework"
    s.requires_arc         = true
s.libraries = 'c++'
s.platform = :ios
    s.ios.deployment_target = '11.0'
end
