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

- (void)isAvailableTouchID;

- (void)didAuthenticationTouchIDWithMessage:(NSString*)message;
- (void)didAuthenticationTouchIDWithMessage:(NSString*)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback;
- (void)didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:(NSString*)message;
- (void)didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:(NSString*)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback;
@end
