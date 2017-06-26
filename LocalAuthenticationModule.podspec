Pod::Spec.new do |spec|
  spec.name         = 'LocalAuthenticationModule'
  spec.version      = '1.0.0'
  spec.license      = { :type => 't5online' }
  spec.homepage     = 'https://github.com/t5online-inc/LocalAuthenticationModule'
  spec.authors      = { 't5online' => 'hjkwak@t5online.com' }
  spec.summary      = 'LocalAuthenticationModule(Nebula)'
  spec.source       = { :git => 'https://github.com/t5online-inc/LocalAuthenticationModule.git' }
  spec.source_files = 'ios/LocalAuthenticationModule/Shared/**/*.{h,m}'
  spec.framework    = 'Foundation', 'UIKit'
end
