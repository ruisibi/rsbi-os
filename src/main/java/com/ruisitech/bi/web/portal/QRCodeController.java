/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.portal;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具
 * @author zxd
 * @Date 2019年8月23日
 */
@Controller
@RequestMapping(value = "/portal")
public class QRCodeController {
	
	private static Logger log = Logger.getLogger(QRCodeController.class);

	@RequestMapping(value = "/generateqrcode.action", method = RequestMethod.GET)
	public @ResponseBody
    void generateQRCode4Product(String url, HttpServletRequest request, HttpServletResponse response) {
		try {
			String longUrl = RSBIUtils.unescape(url);
			// 生成二维码
			response.setHeader("Cache-Control", "no-store");
	        // 不设置缓存
	        response.setHeader("Pragma", "no-cache");
	        response.setDateHeader("Expires", 0);
	        response.setContentType("image/png");
	        //设置图片的文字编码以及内边框
	        Map<EncodeHintType, Object> hints = new HashMap<>();
	        //编码
	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	        //边框距
	        hints.put(EncodeHintType.MARGIN, 0);
	        BitMatrix bitMatrix;
            //参数分别为：编码内容、编码类型、图片宽度、图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(longUrl, BarcodeFormat.QR_CODE, 130, 130, hints);
			// 将二维码输出到页面中
			MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
		} catch (Exception e) {
			log.error("生成二维码异常。", e);
		}
	}
}
