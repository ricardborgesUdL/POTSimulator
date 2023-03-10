package cat.udl.cig.ecc;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.fields.BinaryField;
import cat.udl.cig.fields.BinaryFieldElement;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.RingElement;
import cat.udl.cig.utils.bfarithmetic.QuadraticEquations;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Models an <i>Elliptic Curve</i> \(E\) of the form \(y^{2} + xy = x^{3} + ax^2
 * + b\) if it's non-supersingular, or \(y^{2} + cy = x^{3} + ax + b\) if it's
 * supersingular over a Binary Field.
 *
 * @see cat.udl.cig.fields.BinaryField
 * @author Ricard Garra
 */
public class BinaryEC extends GeneralEC {

    /**
     * Auxiliar constant.
     */
    final static BigInteger TWO = BigInteger.valueOf(2);

    /**
     * A boolean to indicate if {@code this} <i>BinaryEC</i> is super singular
     * or non-super singular. If {@code isSuperSingular == true} then,
     * {@code this} <i>BinaryEC</i> is super singular. Otherwise, {@code this}
     * <i>BinaryEC</i> is non-super singular
     */
    private boolean isSuperSingular;

    /**
     * Creates a copy of the <i>BinaryEC</i> \(E\). This constructor makes a
     * deep copy of \(E\).
     *
     * @param E
     *            the <i>BinaryEC</i> to be copied.
     */
    public BinaryEC(final BinaryEC E) {
        super(E);
        isSuperSingular = E.isSuperSingular;
    }

    /**
     * Creates a <i>BinaryEC</i> over the <i>BinaryField</i> \(K\), with the
     * coefficients stored in the parameter {@code coefficients} and which
     * cardinality factorises in the factors stored in the parameter
     * {@code cardFactors}. The coefficients are <i>BinaryFieldElements</i> and
     * they must belong to the <i>BinaryField</i> \(K\).
     *
     * @param K
     *            the <i>BinaryField</i> over which {@code this} <i>BinaryEC</i>
     *            is defined.
     * @param coefficients
     *            an ArrayList that contains the coefficients of {@code this}
     *            <i>BinaryEC</i>.
     * @param cardFactors
     *            an ArrayList that contains the factors of the cardinality of
     *            {@code this} <i>BinaryEC</i>.
     * @see BinaryField
     * @see BinaryFieldElement
     */
    public BinaryEC(final BinaryField K,
            final BinaryFieldElement[] coefficients,
            final ArrayList<BigInteger> cardFactors) {
        super(K, coefficients, cardFactors, IsACorrectCurve(K,
            coefficients, cardFactors));
        if (k != null) {
            BinaryFieldElement elem = coefficients[0];
            BigInteger aux = elem.getGroup().getSize();
            aux = aux.add(BigInteger.ONE);
            BigInteger t = aux.subtract(getSize());
            isSuperSingular = t.mod(TWO).equals(BigInteger.ZERO);

            if ((isSuperSingular && coefficients.length != 3)
                    || (!isSuperSingular && coefficients.length != 2)) {
                // this.k = null;
                // this.coefficients = null;
                // cardinality = null;
                // this.cardFactors = null;
                isSuperSingular = false;
                // generator = null;
            }
        }
    }

    private static boolean IsACorrectCurve(final BinaryField K,
            final BinaryFieldElement[] coefficients,
            final ArrayList<BigInteger> cardFactors) {
        if (K != null) {
            // BinaryFieldElement elem = coefficients[0];
            BigInteger aux = coefficients[0].getGroup().getSize();
            aux = aux.add(BigInteger.ONE);
            BigInteger aux2 = BigInteger.ONE;
            for (int i = 0; i < cardFactors.size(); i++) {
                aux = aux.multiply(cardFactors.get(i));
            }
            BigInteger t = aux.subtract(aux2);
            boolean isSuperSingularaux =
                    t.mod(TWO).equals(BigInteger.ZERO);

            if ((isSuperSingularaux && coefficients.length != 3)
                    || (!isSuperSingularaux && coefficients.length != 2)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Creates a <i>BinaryEC</i> over the <i>BinaryField</i> \(K\), with the
     * coefficients stored in the parameter {@code coefficients} and which
     * cardinality is stored in the parameter {@code order}. The coefficients
     * are <i>BinaryFieldElements</i> and they must belong to the
     * <i>BinaryField</i> \(K\).
     *
     * @param K
     *            the <i>BinaryField</i> over which {@code this} <i>BinaryEC</i>
     *            is defined.
     * @param coefficients
     *            an ArrayList that contains the coefficients of {@code this}
     *            <i>BinaryEC</i>.
     * @param order
     *            the cardinality of {@code this} <i>BinaryEC</i>.
     * @see BinaryField
     * @see BinaryFieldElement
     */
    /*
     * public BinaryEC(BinaryField K, BinaryFieldElement[] coefficients,
     * BigInteger order) { super(K, coefficients, order); if(this.k != null) {
     * BinaryFieldElement elem = coefficients[0]; BigInteger aux =
     * elem.getRing().getSize(); aux=aux.add(BigInteger.ONE); BigInteger t =
     * aux.subtract(cardinality); isSuperSingular =
     * t.mod(TWO).equals(BigInteger.ZERO); if((isSuperSingular &&
     * coefficients.length != 3) || (!isSuperSingular && coefficients.length !=
     * 2)) { this.k = null; this.coefficients = null; cardinality = null;
     * this.cardFactors = null; isSuperSingular = false; generator = null; }
     * else { //generator = computeGenerator(); } } }
     */

    /*
     * public BinaryEC(BinaryField K, BinaryFieldElement[] coefficients, boolean
     * isSingular) { super(K, coefficients); if(this.k != null) {
     * isSuperSingular = isSingular; if((isSuperSingular && coefficients.length
     * != 3) || (!isSuperSingular && coefficients.length != 2)) { this.k = null;
     * this.coefficients = null; cardinality = null; this.cardFactors = null;
     * isSuperSingular = false; generator = null; } else { //generator =
     * computeGenerator(); } } }
     */

    /**
     * Returns the \(c\) coefficient when {@code this} <i>BinaryEC</i> is a
     * super singular <i>Elliptic Curve</i> and null otherwise.
     *
     * @return a <i>BinaryFieldElement</i> representing the \(c\) coefficient
     *         when {@code this} <i>BinaryEC</i> is a super singular <i>Elliptic
     *         Curve</i> and null, otherwise.
     */
    public BinaryFieldElement getC() {
        return (BinaryFieldElement) (isSuperSingular ? coefficients[2]
                : null);
    }

    @Override
    public BinaryECPoint getNeuterElement() {
        return new BinaryECPoint(this);
    }

    // @Override
    //public EC getIsogenous(final int l) 
    //{
        // throw new UnsupportedOperationException("Not supported yet.");
    //}

    /**
     * Returns if {@code this} <i>BinaryEC</i> is super singular.
     *
     * @return {@code true} if {@code this} <i>BinaryEC</i> is super singular
     *         and {@code false}, otherwise.
     */
    public boolean isSuperSingularEC() {
        return isSuperSingular;
    }

    @Override
    public boolean isOnCurve(final ECPoint iP) {
        BinaryECPoint P = (BinaryECPoint) iP;
        if (!P.E.equals(this)) {
            return false;
        }
        BinaryFieldElement x = P.getX();
        BinaryFieldElement y = P.getY();

        return isOnCurveAux(x, y);
    }

    @Override
    public boolean isOnCurve(final RingElement x2, final RingElement y2) {
        BinaryFieldElement x = (BinaryFieldElement) x2;
        BinaryFieldElement y = (BinaryFieldElement) y2;
        if (!getRing().equals(x.getGroup())
                || !x.getGroup().equals(y.getGroup())) {
            return false;
        }

        return isOnCurveAux(x, y);
    }

    /**
     * Auxiliar method to check if point \(P = (x, y)\) belongs to {@code this}
     * <i>BinaryEC</i> \(E\).
     *
     * @param x
     *            a BinaryFieldElement representing the first coordinate of the
     *            point \(P\).
     * @param y
     *            a BinaryFieldElement representing the second coordinate of the
     *            point \(P\).
     * @return {@code true} if \(P = (x, y) \in E(K)\); {@code false} otherwise.
     */
    private boolean isOnCurveAux(final BinaryFieldElement x,
            final BinaryFieldElement y) {
        if (x == null || y == null) {
            return false;
        }
        BinaryFieldElement leftPart;
        BinaryFieldElement rightPart;
        try {
            leftPart = y.square();
            rightPart = x.pow(BigInteger.valueOf(3));
            rightPart = rightPart.add(getB());
            // y^{2} + cy = x^{3} + ax + b
            if (isSuperSingular) {
                leftPart = leftPart.add(getC().multiply(y));

                rightPart = rightPart.add(getA().multiply(x));

            } else { // y^{2} + xy = x^{3} + ax^2 + b
                leftPart = leftPart.add(x.multiply(y));

                BinaryFieldElement aux = x.square();
                aux = aux.multiply(getA());
                rightPart = rightPart.add(aux);
            }
        } catch (IncorrectRingElementException ex) {
            return false;
        }
        return leftPart.equals(rightPart);
    }

    /*
     * y^2 + xy = x^3 + ax^2 + b if it's non-supersingular, or y^2 + cy = x^3 +
     * ax + b if it's supersingular INPUT: an x-coordinate of the curve OUTPUT:
     * a BinaryECPoint with the x coordinate and its corresponding y coordinate,
     * if it exists. Returns null otherwise
     */
    @Override
    public BinaryECPoint liftX(final RingElement x1) {
        BinaryFieldElement x = (BinaryFieldElement) x1;
        BinaryFieldElement d;
        // we will need to solve a quadratic equation of the form y^2 + y + d =
        // 0
        // it's necessary to convert the curve equation to this form
        try {
            d = x.pow(BigInteger.valueOf(3));
            d = d.add(getB());
            if (isSuperSingular) { // y^2 + cy = x^3 + ax + b
                d = d.add(getA().multiply(x));
                // we need to convert from y^2+cy+d=0 to h^2+h+z=0
                // we divide by c^2 and change variable, so we solve
                // h^2+h+(d/c^2)
                // and then we have that y = h*c
                d = d.divide(getC().square());
            } else { // y^2 + xy = x^3 + ax^2 + b
                d = d.add(getA().multiply(x.square()));
                // in this case, we have y^2+xy+d=0, so we divide by x^2
                // and then y = h*x
                d = d.divide(x.square());
            }

            // finds the solution to an equation of the form h^2 + h + d = 0,
            BinaryFieldElement h = QuadraticEquations.solveQuadratic(d);
            if (h == null) {
                return null;
            }
            BinaryECPoint P;
            if (isSuperSingular) {
                // final solution is y=h*c
                P =
                        new BinaryECPoint(this, x, h.multiply(getC()),
                            BigInteger.ONE); // we let the constructor find the
                // order
            } else {
                // final solution is y=h*x
                P =
                        new BinaryECPoint(this, x, h.multiply(x),
                            BigInteger.ONE);
            }
            // sanity check
            return isOnCurve(P) ? P : null;
            // return P;
        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        String content;
        /*
         * if(!isInitialized()) { content = "Elliptic Curve not initialized"; }
         * else {
         */
        if (isSuperSingular) {
            content =
                    "Elliptic Curve: y\u00B2 + " + getC().toString()
                    + "y = x\u00B3 + (" + getA().toString() + ")x + ("
                    + getB().toString() + ")";
        } else {
            content =
                    "Elliptic Curve: y\u00B2 + xy = x\u00B3 + ("
                            + getA().toString() + ")x\u00B2 + ("
                            + getB().toString() + ")";
        }
        // }
        return content;
    }

    /**
     * @see cat.udl.cig.fields.Group#getSize()
     */
    @Override
    public BigInteger getSize() {
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < cardFactors.size(); i++) {
            result = result.multiply(cardFactors.get(i));
        }
        return result;
    }

    /**
     * @see cat.udl.cig.fields.Group#toElement(java.lang.Object)
     */
    @Override
    public BinaryECPoint toElement(final Object input) {
        BinaryFieldElement xinput =
            (BinaryFieldElement) k.toElement(input);
        return liftX(xinput);
    }

    /**
     * @see cat.udl.cig.fields.Group#multiply(cat.udl.cig.fields.GroupElement,
     *      cat.udl.cig.fields.GroupElement)
     */
    @Override
    public BinaryECPoint multiply(final GroupElement x,
            final GroupElement y) {
        return (BinaryECPoint) x.multiply(y);
    }

    /**
     * @see cat.udl.cig.fields.Group#pow(cat.udl.cig.fields.GroupElement,
     *      java.math.BigInteger)
     */
    @Override
    public BinaryECPoint pow(final GroupElement x, final BigInteger pow) {
        return (BinaryECPoint) x.pow(pow);
    }

    /**
     * @see cat.udl.cig.ecc.EC#getRing()
     */
    @Override
    public BinaryField getRing() {
        return (BinaryField) k;
    }

    /**
     * @see cat.udl.cig.ecc.EC#getA()
     */
    @Override
    public BinaryFieldElement getA() {
        return (BinaryFieldElement) coefficients[0];
    }

    /**
     * @see cat.udl.cig.ecc.EC#getB()
     */
    @Override
    public BinaryFieldElement getB() {
        return (BinaryFieldElement) coefficients[1];
    }

    /**
     * @see cat.udl.cig.ecc.EC#getRandomElement()
     */
    @Override
    public BinaryECPoint getRandomElement() {
        BinaryFieldElement x;
        boolean incorrecte = true;
        BinaryECPoint P = null;
        while (incorrecte) {
            x = (BinaryFieldElement) k.getRandomElement();
            P = liftX(x);
            if (!(P == null) && isOnCurve(P)) {
                incorrecte = false;
            }
        }
        return P;
    }

}
