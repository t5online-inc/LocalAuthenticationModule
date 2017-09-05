//
//  AppDelegate.m
//  BioAuthenticationModule
//
//  Created by JoAmS on 2017. 9. 5..
//  Copyright © 2017년 t5online. All rights reserved.
//

#import "AppDelegate.h"
#import <NebulaCore/Nebula.h>
#import <NebulaCore/NBPluginService.h>
#import "BioAuthenticationPlugin.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

#pragma mark - NebulaDelegate Methods
- (void)loadPlugins {
    NBPluginService* pluginService = [Nebula serviceWithKey:SERVICE_KEY_PLUGIN];
    [pluginService addPluginClass:@"BioAuthenticationPlugin" actionGroup:PLUGIN_GROUP_BIO_AUTHENTICATION];
}


@end
