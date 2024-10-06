package com.example.sudoku.solver;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {
    // Caricamento della libreria nativa di OpenCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // Percorso dell'immagine
        String imagePath = "C:\\Users\\leona\\Desktop\\Progetti\\Java\\SOLVERS\\SudokuSolver\\src\\main\\resources\\sudoku.png";

        // Usa OpenCV per caricare e pre-processare l'immagine
        Mat image = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        if (image.empty()) {
            System.out.println("Errore nel caricamento dell'immagine!");
            return;
        }

        // Converti l'immagine in scala di grigi
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Applica un filtro Gaussiano per ridurre il rumore
        Mat blurredImage = new Mat();
        Imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);

        // Applica una soglia adattiva per binarizzare
        Mat binaryImage = new Mat();
        Imgproc.adaptiveThreshold(blurredImage, binaryImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);

        // Salva l'immagine binarizzata per verifica (opzionale)
        Imgcodecs.imwrite("C:\\Users\\leona\\Desktop\\Progetti\\Java\\SOLVERS\\SudokuSolver\\src\\main\\resources\\sudoku_processed.png", binaryImage);

        // Usa Tesseract per fare OCR sull'immagine pre-processata
        ITesseract tesseract = new Tesseract();

        // Imposta il percorso ai dati di Tesseract
        tesseract.setDatapath("C:\\Users\\leona\\Desktop\\Progetti\\Java\\SOLVERS\\SudokuSolver\\tessdata\\");

        // Imposta la lingua su "eng" e limita ai numeri
        tesseract.setLanguage("eng");
        tesseract.setTessVariable("tessedit_char_whitelist", "123456789");

        // Imposta il PSM su 6 (un blocco uniforme)
        tesseract.setPageSegMode(6);

        try {
            // Passa l'immagine a Tesseract per l'OCR
            String ocrResult = tesseract.doOCR(new java.io.File("C:\\Users\\leona\\Desktop\\Progetti\\Java\\SOLVERS\\SudokuSolver\\src\\main\\resources\\sudoku_processed.png"));

            // Post-processamento per sostituire spazi vuoti o errori con "_"
            String processedResult = processOCRResult(ocrResult);
            System.out.println("Testo riconosciuto con celle vuote come '_':");
            System.out.println(processedResult);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    // Funzione per processare il risultato OCR e inserire "_" dove necessario
    public static String processOCRResult(String ocrResult) {
        // Rimuove spazi extra e sostituisce le celle vuote o errate con "_"
        String[] lines = ocrResult.split("\n");
        StringBuilder processed = new StringBuilder();

        for (String line : lines) {
            line = line.replaceAll("[^0-9]", "_");  // sostituisce qualsiasi carattere non numerico con "_"
            processed.append(line).append("\n");
        }

        return processed.toString();
    }
}
