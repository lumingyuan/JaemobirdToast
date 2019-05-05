import 'dart:async';

import 'package:flutter/services.dart';

class Toast {
  static const MethodChannel _channel = const MethodChannel('toast');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static showToast(String text, {bool success = false, int time = 2000}) async {
    await _channel.invokeMethod(
        'showToast', {'text': text ?? "", 'success': success, 'time': time});
  }

  static showLoading(String text) async {
    await _channel.invokeMethod('showLoading', {'text': text ?? ""});
  }

  static hideLoading() async {
    bool ret = await _channel.invokeMethod('hideLoading');
    print('hideLoading $ret');
  }
}
