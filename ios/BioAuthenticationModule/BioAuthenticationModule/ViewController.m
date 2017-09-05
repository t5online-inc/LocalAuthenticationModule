//
//  ViewController.m
//  BioAuthenticationModule
//
//  Created by JoAmS on 2017. 9. 5..
//  Copyright © 2017년 t5online. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString* url = @"http://www.t5online.com:9080/nebula/test/bioauth.html";
    NSURLRequest* request = [[NSURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
    [[self webView] loadRequest:request];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
