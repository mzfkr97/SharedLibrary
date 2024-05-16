Pod::Spec.new do |s|
    s.name                     = 'shared'
    s.version                  = '1.0.7'
    s.homepage                 = 'https://github.com/mzfkr97/SharedLibrary.git'
    s.source                   = { :http => "https://github.com/mzfkr97/SharedLibrary/raw/master/shared.zip" }
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'MIT'
    s.summary                  = 'ServiceChannel mobile team'
    s.vendored_frameworks = 'shared.framework'
    s.requires_arc         = true
    s.libraries = 'c++'
    s.platform = :ios
    s.ios.deployment_target = '14.0'

end
