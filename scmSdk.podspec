Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.30'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :http => 'https://github.com/mzfkr97/SharedLibrary/raw/master/scmSdk.xcframework.zip', :flatten => false } 
spec.source_files                 = "scmSdk.xcframework/*/scmSdk.framework/Headers/*.{h,m,swift}"
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
spec.vendored_frameworks          = "scmSdk.framework"
  spec.preserve_paths               = "*"
spec.requires_arc                 = true
  spec.swift_version                = "5.0"
    spec.summary                  = 'Mobile team'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
end
