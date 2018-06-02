package com.iss.shop.util;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

public class VerificationUtil {
    public static void createVerification(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String[] codeSequence = {"1","2","3","4","5","6","7","8","9","A","a","B","b","C","c","D","d",
                "E","e","F","f","G","g","H","h","I","i","J","j","K","k","L","l","M","m","N","n","O","o",
                "P","p","Q","q","R","r","S","s","T","t","U","u","V","v","W","w","X","x","Y","y","Z","z"};
        StringBuffer stringBuffer = new StringBuffer();
        //图片的大小
        int width =80, height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获得用于输出文字的Graphics对象
        Graphics g = image.getGraphics();
        Random random = new Random();
        // 随机设置要填充的背景颜色
        g.setColor(getRandomColor(180, 250));
        //设置要填充字母的字体及大小
        g.setFont(new Font(null, Font.BOLD + Font.ITALIC, 18));
        // 填充图形背景
        g.fillRect(0, 10, width, height);
        //生成干扰线
        for(int i=0;i<155;i++){
            int x=random.nextInt(width);
            int y=random.nextInt(height);
            int x1=random.nextInt(width);
            int y1=random.nextInt(height);
            g.drawLine(x, y, x+x1, y+y1);
        }

        for(int i = 0;i<4;i++){
            // 随机获得当前验证码的字符串
            String code = String.valueOf(codeSequence[random.nextInt(27)]);
            // 随机设置当前验证码字符的颜色
            g.setColor(getRandomColor(10, 100));
            // 在图形上输出验证码字符，x和y都是随机生成的
            g.drawString(code, 16 * i + random.nextInt(7), height - random.nextInt(6));
            stringBuffer.append(code);
        }
        //获得HttpSession对象
        HttpSession session = request.getSession();
        //设置session对象5分钟失效
        session.setMaxInactiveInterval(5*60);
        //将验证码保存在session对象中,key为validation_code
        session.setAttribute("validation_code", stringBuffer.toString());
        //关闭Graphics对象
        g.dispose();

        OutputStream outS = response.getOutputStream();

        ImageIO.write(image, "JPEG", outS);
    }

    private static Color getRandomColor(int minColor, int maxColor) {
        Random random = new Random();
        // 保存minColor最大不会超过255
        if (maxColor > 255)
            maxColor = 255;
        // 获得红色的随机颜色值
        int red = minColor + random.nextInt(maxColor - minColor);
        // 获得绿色的随机颜色值
        int green = minColor + random.nextInt(maxColor - minColor);
        // 获得蓝色的随机颜色值
        int blue = minColor + random.nextInt(maxColor - minColor);
        return new Color(red, green, blue);
    }

}
