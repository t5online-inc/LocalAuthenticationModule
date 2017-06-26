//
//  LocalAuthenticationPlugin.m
//  LocalAuthenticationModule
//
//  Created by Hyunjun Kwak on 2017. 6. 20..
//  Copyright © 2017년 Hyunjun Kwak. All rights reserved.
//

#import "LocalAuthenticationPlugin.h"
#import <LocalAuthentication/LocalAuthentication.h>
#import <NebulaCore/NSObject+Json.h>

#define kLAErrorUnsupportedDevice   -99

NSString* const kPluginStatusCodeFailure = @"500";

@implementation LocalAuthenticationPlugin

/**
 isAvailableTouchID
 */
- (void)isAvailableTouchID {
    Class laContaxtClass = NSClassFromString(@"LAContext");
    
    if (laContaxtClass) {
        NSError* error = nil;
        LAContext* laContext = [[LAContext alloc] init];
        BOOL isAvailable = [laContext canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error];
        
        if (isAvailable) {
            [self resolve];
        } else {
            NSString* message = [self authenticationErrorMessage:[error code]];
            [self reject:kPluginStatusCodeFailure message:message data:nil];
        }
    } else {
        NSString* message = [self authenticationErrorMessage:kLAErrorUnsupportedDevice];
        [self reject:kPluginStatusCodeFailure message:message data:nil];
    }
}

/**
 didAuthenticationTouchIDWithMessage:

 @param message Localized Reason Message
 */
- (void)didAuthenticationTouchIDWithMessage:(NSString*)message {
    [self didAuthenticationTouchIDWithMessage:message cancelTitle:nil fallbackTitle:nil];
}

/**
 didAuthenticationTouchIDWithMessage:cancelTitle:fallbackTitle:

 @param message Localized Reason Message
 @param cancel Localized Cancel Button Title
 @param fallback Localized Fallback Button Title
 */
- (void)didAuthenticationTouchIDWithMessage:(NSString*)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback {
    [self authenticationTouchIDLaPolicy:LAPolicyDeviceOwnerAuthentication withMessage:message cancelTitle:cancel fallbackTitle:fallback];
}

/**
 didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:

 @param message Localized Reason Message
 */
- (void)didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:(NSString*)message {
    [self didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:message cancelTitle:nil fallbackTitle:nil];
}

/**
 didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:cancelTitle:fallbackTitle

 @param message Localized Reason Message
 @param cancel Localized Cancel Button Title
 @param fallback Localized Fallback Button Title
 */
- (void)didAuthenticationTouchIDAndCustomPassCodeFallbackWithMessage:(NSString*)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback {
    [self authenticationTouchIDLaPolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics withMessage:message cancelTitle:cancel fallbackTitle:fallback];
}

/**
 authenticationTouchIDLaPolicy:withMessage:cancelTitle:fallbackTitle

 @param policy LAPolicy
 @param message Localized Reason Message
 @param cancel Localized Cancel Button Title
 @param fallback Localized Fallback Button Title
 */
- (void)authenticationTouchIDLaPolicy:(LAPolicy)policy withMessage:(NSString *)message cancelTitle:(NSString*)cancel fallbackTitle:(NSString*)fallback  {
    Class laContaxtClass = NSClassFromString(@"LAContext");
    
    if (laContaxtClass) {
        NSError* error = nil;
        LAContext* laContext = [[LAContext alloc] init];
        BOOL isAvailable = [laContext canEvaluatePolicy:policy error:&error];
        
        if (cancel) {
            [laContext setLocalizedCancelTitle:cancel];
        }
        
        if (fallback) {
            [laContext setLocalizedFallbackTitle:fallback];
        }
        
        if (isAvailable) {
            [laContext evaluatePolicy:policy localizedReason:message reply:^(BOOL success, NSError * _Nullable error) {
                if (success) {
                    [self resolve];
                } else {
                    NSString* message = [self authenticationErrorMessage:[error code]];
                    [self reject:kPluginStatusCodeFailure message:message data:nil];
                }
            }];
        } else {
            NSString* message = [self authenticationErrorMessage:[error code]];
            [self reject:kPluginStatusCodeFailure message:message data:nil];
        }
    } else {
        NSString* message = [self authenticationErrorMessage:kLAErrorUnsupportedDevice];
        [self reject:kPluginStatusCodeFailure message:message data:nil];
    }
}

#pragma mark -

/**
 authenticationErrorMessage

 @param code ErrorCode
 @return ErrorMessage
 */
- (NSString*)authenticationErrorMessage:(NSInteger)code {
    switch (code) {
        case kLAErrorAuthenticationFailed:
            return @"authenticationFailed";
            
        case kLAErrorUserCancel:
            return @"userCancel";
            
        case kLAErrorUserFallback:
            return @"userFallback";
            
        case kLAErrorSystemCancel:
            return @"systemCancel";
            
        case kLAErrorPasscodeNotSet:
            return @"passcodeNotSet";
            
        case kLAErrorTouchIDNotAvailable:
            return @"touchIDNotAvailable";
            
        case kLAErrorTouchIDNotEnrolled:
            return @"touchIDNotEnrolled";
            
        case kLAErrorTouchIDLockout:
            return @"touchIDLockout";
            
        case kLAErrorAppCancel:
            return @"appCancel";
            
        case kLAErrorUnsupportedDevice:
            return @"unsupportedDevice";
    }
    
    return @"unknown";
}

@end
