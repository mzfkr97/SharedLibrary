Pod::Spec.new do |s|
    s.name                     = 'shared'
    s.version                  = '1.0.7'
    s.homepage                 = 'https://github.com/mzfkr97/SharedLibrary.git'
    s.source                   = { :git => "https://github.com/mzfkr97/SharedLibrary.git", :branch => 'master' }
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'MIT'
    s.summary                  = 'ServiceChannel mobile team'
    s.source_files = 'shared.framework/Headers/scmSdk.h'
    s.vendored_frameworks = 'shared.framework'
    s.requires_arc         = true
    s.libraries = 'c++'
    s.platform = :ios
    s.ios.deployment_target = '14.0'

end
