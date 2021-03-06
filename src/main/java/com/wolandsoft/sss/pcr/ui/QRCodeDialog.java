/*
    Copyright 2017 Alexander Shulgin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.wolandsoft.sss.pcr.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JDialog;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QR modal dialog
 */
public class QRCodeDialog extends JDialog {

    private static final long serialVersionUID = -6457209361778290740L;

    public QRCodeDialog(String strToEncode) throws WriterException {
	super((Frame) null, EStrings.lbl_app_name.toString(), true);

	Dimension Size = Toolkit.getDefaultToolkit().getScreenSize();
	int size = (int) Math.min(Size.getWidth() / 2, Size.getHeight() / 2);
	Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
	hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

	// Now with zxing version 3.2.1 you could change border size (white
	// border size to just 1)
	hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
	hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

	QRCodeWriter qrCodeWriter = new QRCodeWriter();
	BitMatrix byteMatrix = qrCodeWriter.encode(strToEncode, BarcodeFormat.QR_CODE, size, size, hintMap);
	int CrunchifyWidth = byteMatrix.getWidth();
	BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
	image.createGraphics();

	Graphics2D graphics = (Graphics2D) image.getGraphics();
	graphics.setColor(Color.WHITE);
	graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
	graphics.setColor(Color.BLACK);

	for (int i = 0; i < CrunchifyWidth; i++) {
	    for (int j = 0; j < CrunchifyWidth; j++) {
		if (byteMatrix.get(i, j)) {
		    graphics.fillRect(i, j, 1, 1);
		}
	    }
	}

	QRCodePanel imgPanel = new QRCodePanel(image);
	imgPanel.setPreferredSize(new Dimension(size, size));
	setResizable(false);
	getContentPane().add(imgPanel);
	pack();
	setLocation(new Double((Size.getWidth() / 2) - (getWidth() / 2)).intValue(),
		new Double((Size.getHeight() / 2) - (getHeight() / 2)).intValue());
    }
}
