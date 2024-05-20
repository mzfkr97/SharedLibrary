Pod::Spec.new do |spec|
    spec.name                     = 'scmSdk'
    spec.version                  = '7.7.53'
    spec.homepage                 = 'https://github.com/mzfkr97'
    spec.source       = { :http => 'https://github.com/mzfkr97/SharedLibrary/raw/master/scmSdk.framework.zip' }
    spec.authors                  = 'Service channel mobile team'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Mobile team'
    spec.static_framework         = false
    spec.ios.deployment_target = '11.0'
    spec.source_files = 'scmSdk/**/*.{h,m}'
    spec.public_header_files = 'scmSdk/**/*.h'
    spec.vendored_frameworks = 'scmSdk.framework'
end
