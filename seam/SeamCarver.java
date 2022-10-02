/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 08/21/2022
 *  Description: Seam Carver
 * TODO : avoid trasnposing energy unnecessery
 * TODO: use system.arraycopy, use int[][] for picture
 * TODO: recalculate energy for affected cells without transpose
 * TODO: use column major for finding the seams
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private int width;
    private int height;
    private boolean hseam = false;
    private int[][] pixels;    // 2d array of rgb values representing the picture
    private boolean transposed = false;
    private boolean eTransposed = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("picture is null");
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.energy = new double[height][width];
        createPixelArray();
        createEnergyMatrix();
    }

    private void createPixelArray() {
        pixels = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixels[row][col] = picture.getRGB(col, row);
            }
        }

    }

    private void createEnergyMatrix() {

        double[][] e = new double[height][width];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                e[row][col] = energy(col, row);
            }
        }

        energy = e;
    }

    // current picture
    // todo : get clone of current picture
    public Picture picture() {
        return clonePicture();
    }

    // create a picture with updated height
    private Picture clonePicture() {
        Picture p = new Picture(width, height);
        // copy pixels from current picture to
        // new copy
        if (transposed) {
            pixels = transpose(pixels);
            transposed = false;
        }
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                p.setRGB(col, row, pixels[row][col]);
            }
        }

        return p;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (eTransposed) {
            energy = transposeEnergy(energy);
            eTransposed = false;
        }

        if (isOutOfBound(x, y)) throw new IllegalArgumentException("out side of the range");

        if (energy[y][x] != 0) {
            return energy[y][x];
        }
        else {
            if (isBorder(x, y, width, height)) {
                energy[y][x] = 1000.00;
            }
            else {
                if (transposed) {
                    pixels = transpose(pixels);
                    transposed = false;
                }
                energy[y][x] = Math.sqrt(deltaX(x, y) + deltaY(x, y));
            }
        }


        return energy[y][x];

    }

    private boolean isOutOfBound(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return true;
        return false;
    }

    // returns square of X gradient
    private double deltaX(int x, int y) {
        int rgbx1 = pixels[y][x + 1];
        int rgbx2 = pixels[y][x - 1];

        double sqrR = Math.pow(getRed(rgbx1) - getRed(rgbx2), 2);
        double sqrG = Math.pow(getGreen(rgbx1) - getGreen(rgbx2), 2);
        double sqrB = Math.pow(getBlue(rgbx1) - getBlue(rgbx2), 2);

        double sqrX = sqrR + sqrG + sqrB; // square of x gradient

        return sqrX;

    }

    // returns square of y gradient
    private double deltaY(int x, int y) {
        int rgby1 = pixels[y + 1][x];
        int rgby2 = pixels[y - 1][x];

        double sqrR = Math.pow(getRed(rgby1) - getRed(rgby2), 2);
        double sqrG = Math.pow(getGreen(rgby1) - getGreen(rgby2), 2);
        double sqrB = Math.pow(getBlue(rgby1) - getBlue(rgby2), 2);

        double sqrY = sqrR + sqrG + sqrB; // square of y gradient

        return sqrY;
    }


    private boolean isBorder(int x, int y, int wid, int hi) {
        return (x == 0 || y == 0 || x == wid - 1 || y == hi - 1);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        hseam = true;
        if (!transposed) {
            pixels = transpose(pixels);
            transposed = true;
        }

        if (!eTransposed) {
            energy = transposeEnergy(energy);
            eTransposed = true;
        }

        int[] seam = findSeam(width, height);
        // TODO: transpose back if needed;
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        hseam = false;
        // if the picture is already transposed
        // bring it back to original shape
        if (transposed) {
            pixels = transpose(pixels);
            transposed = false;
        }

        if (eTransposed) {
            energy = transposeEnergy(energy);
            eTransposed = false;
        }
        return findSeam(height, width);
    }

    private int[] findSeam(int heightP, int widthP) {
        double[][] distTo = new double[heightP][widthP];
        int[][] edgeTo = new int[heightP][widthP];

        // initialize distTo for not visited vertices to
        // positive infinity

        for (int row = 0; row < heightP; row++) {
            for (int col = 0; col < widthP; col++) {
                distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }

        // initialize first row to zero
        for (int col = 0; col < widthP; col++) {
            distTo[0][col] = 1000.00;
        }

        // initialize edgeTo
        // initialize first row to same row
        for (int col = 0; col < widthP; col++) {
            edgeTo[0][col] = col;
        }

        double minenergy = Double.MAX_VALUE;
        int minX = -1;
        for (int row = 1; row < heightP; row++) {
            for (int col = 0; col < widthP; col++) {
                // upper left
                if (col > 0) {
                    relax(row - 1, col - 1, row, col, distTo, edgeTo);
                }

                // upper
                relax(row - 1, col, row, col, distTo, edgeTo);

                // upper right
                if (col < widthP - 1) {
                    relax(row - 1, col + 1, row, col, distTo, edgeTo);
                }

                // check for min energy seam in the bottom row
                if (row == heightP - 1 && distTo[row][col] < minenergy) {
                    minenergy = distTo[row][col];
                    minX = col;
                }
            }


        }
        // backtrack the vertices from the last row
        int[] seam = new int[heightP];
        int h = heightP - 1;
        int xindex = minX;
        seam[h] = xindex;
        while (h >= 1) {

            xindex = edgeTo[h][xindex];
            seam[h - 1] = xindex;
            h -= 1;
        }
        return seam;
    }

    // relax the edges
    private void relax(int fromY, int fromX, int toY, int toX, double[][] distTo, int[][] edgeTo) {

        double newDistance = distTo[fromY][fromX] + energy[toY][toX];
        if (distTo[toY][toX] > newDistance) {
            distTo[toY][toX] = newDistance;
            edgeTo[toY][toX] = fromX; // consider one pixel per row/height store the y coordinate
        }
    }

    private int[][] transpose(int[][] pixelP) {

        int newRow = pixelP[0].length;
        int newCol = pixelP.length;

        int[][] transp = new int[newRow][newCol];

        for (int row = 0; row < pixelP.length; row++) {
            for (int col = 0; col < pixelP[0].length; col++) {
                transp[col][row] = pixelP[row][col];
            }
        }

        return transp;
    }


    private double[][] transposeEnergy(double[][] energyP) {
        int transHeight = energyP[0].length;
        int transWidth = energyP.length;
        double[][] transp = new double[transHeight][transWidth];

        for (int row = 0; row < transHeight; row++) {
            for (int col = 0; col < transWidth; col++) {
                transp[row][col] = energyP[col][row];
            }
        }

        return transp;
    }

    // remove horizontal seam from current picture
    // TODO:optimize and correct the output;
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException(" input seam is null");
        if (height <= 1) throw new IllegalArgumentException("height is less");

        if (!transposed) {
            pixels = transpose(pixels);
            transposed = true;
        }

        if (!eTransposed) {
            energy = transposeEnergy(energy);
            eTransposed = true;
        }
        for (int row = 0; row < width; row++) {
            shiftLeft(seam[row], row, height);
        }
        height -= 1;

        recalHseamEnergy(seam);

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException(" input seam is null");
        if (width <= 1) throw new IllegalArgumentException("width is less");

        if (transposed) {
            pixels = transpose(pixels);
            transposed = false;
        }

        if (eTransposed) {
            energy = transposeEnergy(energy);
            eTransposed = false;
        }

        for (int row = 0; row < height; row++) {
            shiftLeft(seam[row], row, width);
        }
        width -= 1;
        recalVseamEnergy(seam);
    }

    // recalculate energy
    private void recalHseamEnergy(int[] seam) {

        for (int row = 0; row < width; row++) {
            int col = seam[row];

            if (!isBorder(col, row, height, width)) {
                energy[row][col] = Math.sqrt(deltaX(col, row) + deltaY(col, row));

                if (!isBorder(col - 1, row, height, width))
                    energy[row][col - 1] = Math.sqrt(deltaX(col - 1, row) + deltaY(col - 1, row));
                else energy[row][col - 1] = 1000;

                // shift unchanged energies to left
                shiftLeftEnergy(col, row, height);
            }
            else {
                if (col == 0) shiftLeftEnergy(col, row, height);
                energy[row][col] = 1000;
            }

        }


    }

    private void recalVseamEnergy(int[] seam) {
        for (int row = 0; row < height; row++) {
            int col = seam[row];

            if (!isBorder(col, row, width, height)) {
                energy[row][col] = Math.sqrt(deltaX(col, row) + deltaY(col, row));

                if (!isBorder(col - 1, row, width, height))
                    energy[row][col - 1] = Math.sqrt(deltaX(col - 1, row) + deltaY(col - 1, row));
                else energy[row][col - 1] = 1000;

                // shift unchanged energies to left
                shiftLeftEnergy(col, row, width);
            }
            else {
                if (col == 0) shiftLeftEnergy(col, row, width);
                energy[row][col] = 1000;
            }

        }
    }

    private void shiftLeft(int col, int row, int wid) {
        if (col == wid - 1) {
            pixels[row][col] = 0;
        }
        else {
            // shift each pixel to left from seam to end
            System.arraycopy(pixels[row], col + 1, pixels[row], col,
                             wid - col - 1);
        }
    }

    private void shiftLeftEnergy(int col, int row, int wid) {
        if (col == wid - 1) {
            energy[row][col] = 1000;
        }
        else {
            // shift each pixel to left from seam to end
            System.arraycopy(energy[row], col + 2, energy[row], col + 1,
                             wid - col - 1);
        }
    }
    // helper methods

    /**
     * Returns the red component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the red component.
     */
    private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    /**
     * Returns the green component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the green component.
     */
    public int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    /**
     * Returns the blue component in the range 0-255 in the default sRGB
     * space.
     *
     * @return the blue component.
     */
    public int getBlue(int rgb) {
        return (rgb) & 0xFF;
    }


    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
