/* *****************************************************************************
 *  Name: Sijo Xavier
 *  Date: 08/21/2022
 *  Description: Seam Carver
 * TODO : avoid trasnposing enrgy unnecessery
 * TODO: use system.arraycopy, use int[][] for picture
 * TODO: recalculate energy for affected cells
 * TODO: use column major for finding the seams
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private Picture picClone;
    private double[][] energy;
    private int width;
    private int height;
    private boolean hseam = false;
    private double[][] transpose;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("picture is null");
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.energy = new double[height][width];
        createEnergyMatrix();
    }

    private void createEnergyMatrix() {

        double[][] e = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                e[i][j] = energy(j, i);
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

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                p.setRGB(col, row, picture.getRGB(col, row));
            }
        }

        return p;
    }

    // width of current picture
    public int width() {
        this.width = picture.width();
        return width;
    }

    // height of current picture
    public int height() {
        this.height = picture.height();
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isOutOfBound(x, y)) throw new IllegalArgumentException("out side of the range");

        if (energy[y][x] != 0) {
            return energy[y][x];
        }
        else {
            if (isBorder(x, y)) {
                energy[y][x] = 1000.00;
            }
            else {
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
        Color cx1 = picture.get(x + 1, y);
        Color cx2 = picture.get(x - 1, y);

        double sqrR = Math.pow(cx1.getRed() - cx2.getRed(), 2);
        double sqrG = Math.pow(cx1.getGreen() - cx2.getGreen(), 2);
        double sqrB = Math.pow(cx1.getBlue() - cx2.getBlue(), 2);

        double sqrX = sqrR + sqrG + sqrB; // square of x gradient

        return sqrX;

    }

    // returns square of y gradient
    private double deltaY(int x, int y) {
        Color cy1 = picture.get(x, y + 1);
        Color cy2 = picture.get(x, y - 1);

        double sqrR = Math.pow(cy1.getRed() - cy2.getRed(), 2);
        double sqrG = Math.pow(cy1.getGreen() - cy2.getGreen(), 2);
        double sqrB = Math.pow(cy1.getBlue() - cy2.getBlue(), 2);

        double sqrY = sqrR + sqrG + sqrB; // square of y gradient

        return sqrY;
    }


    private boolean isBorder(int x, int y) {
        return (x == 0 || y == 0 || x == width - 1 || y == height - 1);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (transpose == null) {
            transpose = transpose(energy);
        }
        hseam = true;
        int[] seam = findSeam(width, height);
        // TODO: transpose back if needed;
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        hseam = false;
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
        for (int i = 1; i < heightP; i++) {
            for (int j = 0; j < widthP; j++) {
                // upper left
                if (j > 0) {
                    relax(i - 1, j - 1, i, j, distTo, edgeTo);
                }

                // upper
                relax(i - 1, j, i, j, distTo, edgeTo);

                // upper right
                if (j < widthP - 1) {
                    relax(i - 1, j + 1, i, j, distTo, edgeTo);
                }

                // check for min energy seam in the bottom row
                if (i == heightP - 1 && distTo[i][j] < minenergy) {
                    minenergy = distTo[i][j];
                    minX = j;
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

        double newDistance = distTo[fromY][fromX] + (hseam ? transpose[toY][toX] :
                                                     energy[toY][toX]);
        if (distTo[toY][toX] > newDistance) {
            distTo[toY][toX] = newDistance;
            edgeTo[toY][toX] = fromX; // consider one pixel per row/height store the y coordinate
        }
    }

    private double[][] transpose(double[][] energyP) {

        int newRow = energy[0].length;
        int newCol = energy.length;

        double[][] transp = new double[newRow][newCol];

        for (int row = 0; row < energy.length; row++) {
            for (int col = 0; col < energy[0].length; col++) {
                transp[col][row] = energyP[row][col];
            }
        }

        return transp;
    }

    private Picture transpose(Picture picture) {
        int transHeight = picture.width();
        int transWidth = picture.height();
        Picture transp = new Picture(transWidth, transHeight);

        for (int row = 0; row < transHeight; row++) {
            for (int col = 0; col < transWidth; col++) {
                transp.setRGB(col, row, picture.getRGB(row, col));
            }
        }

        return transp;
    }

    // remove horizontal seam from current picture
    // TODO:optimize and correct the output;
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException(" input seam is null");
        if (height <= 1) throw new IllegalArgumentException("height is less");

        Picture pic = transpose(picture);
        for (int col = 0; col < height; col++) {
            shiftLeft(col, seam[col], pic, height);
        }
        picture = transpose(pic); // assign edited picture to picture
        height -= 1;

        recalEnergy(seam, true);

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException(" input seam is null");
        if (width <= 1) throw new IllegalArgumentException("width is less");
        Picture pic = picture;
        for (int row = 0; row < height; row++) {
            shiftLeft(seam[row], row, pic, width);
        }
        width -= 1;

        picture = pic; // assign edited picture to picture
        recalEnergy(seam, false);
    }

    // set energy to zero so that call to energy will
    // recalculate energy
    private void recalEnergy(int[] seam, boolean hz) {
        if (!hz) {
            for (int row = 0; row < height; row++) {
                int col = seam[row];
                energy[row][col] = 0;
                if (col != 0 && col != width - 1) {
                    energy[row][col + 1] = 0;
                }

            }
        }
        else {
            for (int row = 0; row < height; row++) {
                int col = seam[row];
                energy[col][row] = 0;
                if (col != 0 && col != width - 1) {
                    energy[col][row] = 0;
                }

            }
        }

    }

    private void shiftLeft(int col, int row, Picture pictureP, int wid) {
        if (col == wid - 1) {
            pictureP.setRGB(col, row, 0);
        }
        else {
            // shift each pixel to left from seam to end
            for (int start = col; start < wid - 1; start++) {
                int rgb = pictureP.getRGB(start + 1, row);
                pictureP.setRGB(start, row, rgb);
                pictureP.setRGB(start + 1, row, 0);
            }
        }
    }


    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
