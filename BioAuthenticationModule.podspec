Pod::Spec.new do |spec|
  spec.name         = 'BioAuthenticationModule'
  spec.version      = '1.0.0'
  spec.license      = { :type => 't5online' }
  spec.homepage     = 'https://github.com/t5online-inc/BioAuthenticationModule'
  spec.authors      = { 't5online' => 'hjkwak@t5online.com' }
  spec.summary      = 'BioAuthenticationModule(Nebula)'
  spec.source       = { :git => 'https://github.com/t5online-inc/BioAuthenticationModule.git' }
  spec.source_files = 'ios/BioAuthenticationModule/Shared/**/*.{h,m}'
  spec.framework    = 'Foundation', 'UIKit'
end
