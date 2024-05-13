Pod::Spec.new do |s|
    s.name                     = 'sharedframework'
    s.version                  = '1.1.10'
    s.homepage                 = 'https://github.com/mzfkr97/SharedLibrary.git'
    s.source       = { :git => "https://github.com/mzfkr97/SharedLibrary.git", :tag => s.version.to_s}
    s.authors                  = 'ServiceChannel mobile team'
    s.license                  = 'MIT'
    s.summary                  = 'ServiceChannel mobile team'
s.source_files                 = "shared.framework"
  s.preserve_paths               = "*"
    s.requires_arc         = true
s.platform = :ios
    s.ios.deployment_target = '11.0'

end
