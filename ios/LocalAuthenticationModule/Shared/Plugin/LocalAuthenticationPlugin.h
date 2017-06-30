//
//  LocalAuthenticationPlugin.h
//  LocalAuthenticationModule
//
//  Created by Hyunjun Kwak on 2017. 6. 20..
//  Copyright © 2017년 Hyunjun Kwak. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <NebulaCore/NBPlugin.h>

#define PLUGIN_GROUP_LOCAL_AUTHENTICATION @"localauthentication"

@interface LocalAuthenticationPlugin : NBPlugin

- (void)isAvailable;

- (void)startAuthentication:(NSString*)message;
//- (void)startAuthentication:(NSString*)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback;
//- (void)startAuthenticationCustomPassCode:(NSString*)message;
//- (void)startAuthenticationCustomPassCode:(NSString*)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback;
@end
