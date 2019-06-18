#import "ToastPlugin.h"
#import <MBProgressHUD.h>

@interface ToastPlugin()
@property(nonatomic, strong) MBProgressHUD* progressHUD;
@property(nonatomic, strong) MBProgressHUD* toastHUD;

@end

@implementation ToastPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"toast"
            binaryMessenger:[registrar messenger]];
  ToastPlugin* instance = [[ToastPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (UIWindow*) window {
    return [UIApplication sharedApplication].keyWindow;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
      
  } else if ([@"showToast" isEqualToString:call.method]) {
      NSString* text = [call.arguments objectForKey:@"text"];
      BOOL success = [[call.arguments objectForKey:@"success"] boolValue];
      int millseconds = [[call.arguments objectForKey:@"time"] intValue];
      dispatch_async(dispatch_get_main_queue(), ^{
          if (success) {
              [self show:text icon:@"success.png" time:millseconds];
          } else {
              [self show:text icon:@"attention.png" time:millseconds];
          }
          result(nil);
      });
  } else if ([@"showLoading" isEqualToString:call.method]) {
      NSString* text = [call.arguments objectForKey:@"text"];
      dispatch_async(dispatch_get_main_queue(), ^{
          [self showLoading:text];
          result(nil);
      });
  } else if ([@"hideLoading" isEqualToString:call.method]) {
      __weak typeof(self) weakSelf = self;
      dispatch_async(dispatch_get_main_queue(), ^{
          if (weakSelf.progressHUD != nil) {
              [weakSelf.progressHUD hideAnimated:YES];
              result(@(YES));
          } else {
              result(@(NO));
          }
      });
  } else {
    result(FlutterMethodNotImplemented);
  }
}

- (void)show:(NSString *)text icon:(NSString *)icon time:(int)millseconds {
    if (_toastHUD != nil) {
        [_toastHUD hideAnimated:YES];
        _toastHUD = nil;
    }
    
    // 快速显示一个提示信息
    _toastHUD = [MBProgressHUD showHUDAddedTo:[self window] animated:YES];
    _toastHUD.detailsLabel.text = text;
    // 设置图片
    _toastHUD.customView = [[UIImageView alloc] initWithImage: [UIImage imageNamed:[NSString stringWithFormat:@"MBProgressHUD.bundle/%@", icon]]];
    // 再设置模式
    _toastHUD.mode = MBProgressHUDModeCustomView;
    _toastHUD.bezelView.color = [UIColor colorWithWhite:0.f alpha:.8f];
    _toastHUD.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
    _toastHUD.contentColor = [UIColor whiteColor];
    _toastHUD.minSize = CGSizeMake(80, 60);
    
    // 隐藏时候从父控件中移除
    _toastHUD.removeFromSuperViewOnHide = YES;
    // 1秒之后再消失
    [_toastHUD hideAnimated:YES afterDelay:millseconds/1000.0f];
}

- (void)showLoading:(NSString*)text {
    if (_progressHUD != nil) {
        [_progressHUD setRemoveFromSuperViewOnHide:YES];
        [_progressHUD hideAnimated:YES];
        _progressHUD = nil;
    }
    
    // 快速显示一个提示信息
    _progressHUD = [MBProgressHUD showHUDAddedTo:[self window] animated:YES];
    _progressHUD.label.text = text;
    // 再设置模式
    _progressHUD.mode = MBProgressHUDModeIndeterminate;
    _progressHUD.bezelView.color = [UIColor colorWithWhite:0.f alpha:.8f];
    _progressHUD.bezelView.style = MBProgressHUDBackgroundStyleSolidColor;
    _progressHUD.contentColor = [UIColor whiteColor];
    _progressHUD.minSize = CGSizeMake(80, 60);
    // 隐藏时候从父控件中移除
    _progressHUD.removeFromSuperViewOnHide = YES;
}

@end
