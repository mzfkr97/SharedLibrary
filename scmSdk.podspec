Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.51'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :http => 'https://github.com/mzfkr97/SharedLibrary/releases/download/7.7.51/scmSdk.framework.zip' }
    spec.authors                  = 'Service channel mobile team'
spec.source_files = 'Pod/Classes'
spec.resources = 'Pod/Assets/*'
  spec.preserve_paths = 'Pod/Scripts/*'
spec.public_header_files = '*.h'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Mobile team'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
end
