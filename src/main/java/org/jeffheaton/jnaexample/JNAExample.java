package org.jeffheaton.jnaexample;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;

public class JNAExample {
   public interface User32 extends StdCallLibrary {
      User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
      boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);
      int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);
   }

   public static void main(String[] args) {
      final User32 user32 = User32.INSTANCE;
      user32.EnumWindows(new WNDENUMPROC() {
         int count = 0;
         @Override
         public boolean callback(HWND hWnd, Pointer arg1) {
            byte[] windowText = new byte[1024];
            user32.GetWindowTextA(hWnd, windowText, windowText.length);
            String wText = Native.toString(windowText);

            if (wText.isEmpty()) {
               return true;
            }

            System.out.println("Window, hwnd:" + hWnd + ", total " + ++count
                  + " Title: " + wText);
            return true;
         }
      }, null);
   }
}
