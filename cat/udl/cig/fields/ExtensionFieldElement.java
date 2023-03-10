package cat.udl.cig.fields;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.utils.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Models an <i>Extension Field Element</i>. This <i>Extension Field Element</i>
 * has a value \(polynomial\), represented by a <i>Polynomial</i>, and belongs
 * to an <i>ExtensionField</i> /(F_{p^{n}}/). Any attempt to operate an instance
 * of <i>ExtensionFieldElement</i> with an instance of a different kind of
 * <i>RingElement</i> causes an runtime exception.
 *
 * @see RingElement
 * @author M.Àngels Cerveró
 */
public class ExtensionFieldElement implements RingElement {
    /**
     * The <i>Extension Field</i> \(\mathbb{F}_{p^{n}}\) in which this
     * <i>Extension Field Element</i> is defined.
     *
     * @see ExtensionField
     */
    private final ExtensionField Fpn;

    /**
     * A Polynomial that encapsulates the value of this <i>Extension Field
     * Element</i>.
     */
    private final Polynomial polynomial;

    /**
     * Creates an <i>ExtensionFieldElement</i> with value polynomial and
     * belonging to the <i>ExtensionField</i> \(Fpn\). If the
     * <i>ExtensionField</i> {@code Fpn} is not initialized, then the created
     * <i>ExtensionFieldElement</i> also remains uninitialized. That is
     * {@code this.Fpn = null} and {@code this.polynomial = null}. This
     * constructor does not make a deep compy of \(Fpn\).
     *
     * @param Fpn
     *            the <i>ExtensionField</i> to which {@code this}
     *            <i>ExtensionFieldElement</i> will belong.
     * @param polynomial
     *            a Polynomial representing the value for {@code this}
     *            <i>ExtensionFieldElement</i>.
     * @see ExtensionField
     */
    public ExtensionFieldElement(final ExtensionField Fpn,
            final Polynomial polynomial) {
        // if(Fpn != null && Fpn.isInitialized() && polynomial != null) {
        this.Fpn = Fpn;
        this.polynomial =
                polynomial.euclideanDivision(Fpn.getReducingPolynomial())
                .getValue();
        /*
         * } else { this.Fpn = null; this.polynomial = null; }
         */
    }

    /**
     * Creates a copy of the <i>ExtensionFieldElement</i> \(q\). If \(q\) is
     * null or uninitialized, {@code this} instance remains uninitialized.
     *
     * @param q
     *            the <i>ExtensionFieldElement</i> to be copied.
     */
    public ExtensionFieldElement(final ExtensionFieldElement q) {
        // if(q != null)&& q.isInitialized()) {
        Fpn = new ExtensionField(q.Fpn);
        polynomial = new Polynomial(q.polynomial);
        /*
         * } else { this.Fpn = null; this.polynomial = null; }
         */
    }

    /*
     * @Override public ExtensionFieldElement cloneElement() { return new
     * ExtensionFieldElement(this); }
     */

    /*
     * @Override public boolean isInitialized() { return (Fpn != null &&
     * polynomial != null); }
     */

    @Override
    public boolean belongsToSameGroup(final GroupElement q) {
        if (q instanceof ExtensionFieldElement) {
            // if(q.isInitialized() && isInitialized()) {
            ExtensionFieldElement q1 = (ExtensionFieldElement) q;
            return Fpn.equals(q1.Fpn);
            // }
        }
        return false;
    }

    @Override
    public ExtensionField getGroup() {
        return Fpn;
    }

    @Override
    public Polynomial getValue() {
        return polynomial;
    }

    @Override
    public BigInteger getIntValue() {
        BigInteger value = BigInteger.ZERO;
        BigInteger term;
        BigInteger charactersitic = Fpn.p;
        for (int i = 0; i <= polynomial.getDegree(); i++) {
            term = charactersitic.pow(i);
            term = term.multiply(polynomial.getCoefficient(i).getValue());
            value = value.add(term);
        }
        return value;
    }

    @Override
    public ExtensionFieldElement add(final RingElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            ExtensionFieldElement q1 = (ExtensionFieldElement) q;
            Polynomial r = polynomial.add(q1.polynomial);
            return new ExtensionFieldElement(Fpn, r);
        } else {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                        + "correct instance of ExtensionFieldElement");
        }
    }

    @Override
    public ExtensionFieldElement subtract(final RingElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            ExtensionFieldElement q1 = (ExtensionFieldElement) q;
            Polynomial r = polynomial.subtract(q1.polynomial);
            return new ExtensionFieldElement(Fpn, r);
        } else {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                        + "correct instance of FiniteFieldElement");
        }
    }

    @Override
    public ExtensionFieldElement multiply(final GroupElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            ExtensionFieldElement q1 = (ExtensionFieldElement) q;
            Polynomial r =
                    polynomial.multiply(q1.polynomial,
                        Fpn.getReducingPolynomial());
            return new ExtensionFieldElement(Fpn, r);
        } else {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                        + "correct instance of FiniteFieldElement");
        }
    }

    @Override
    public ExtensionFieldElement divide(final GroupElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            ExtensionFieldElement q1 = (ExtensionFieldElement) q;
            Polynomial r =
                    polynomial.divide(q1.polynomial,
                        Fpn.getReducingPolynomial());
            return new ExtensionFieldElement(Fpn, r);
        } else {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                        + "correct instance of FiniteFieldElement");
        }
    }

    @Override
    public ExtensionFieldElement opposite() { // throws
        // IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("FiniteFieldElement not " +
         * "initialized"); }
         */
        return new ExtensionFieldElement(Fpn, polynomial.opposite());
    }

    @Override
    public ExtensionFieldElement inverse() { // throws
        // IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("FiniteFieldElement not " +
         * "initialized"); }
         */
        return new ExtensionFieldElement(Fpn, polynomial.inverse(Fpn
            .getReducingPolynomial()));
    }

    @Override
    public ExtensionFieldElement pow(final BigInteger k) {// throws
        // IncorrectRingElementException
        // {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("FiniteFieldElement not " +
         * "initialized"); }
         */

        return new ExtensionFieldElement(Fpn, polynomial.pow(k,
            Fpn.getReducingPolynomial()));
    }

    @Override
    public ArrayList<RingElement> squareRoot() {// throws
        // IncorrectRingElementException
        // {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("FiniteFieldElement not " +
         * "initialized"); }
         */
        ArrayList<RingElement> root = new ArrayList<RingElement>();

        Polynomial p = polynomial.squareRoot(Fpn.getReducingPolynomial());
        if (p.getDegree() != -1) {
            root.add(new ExtensionFieldElement(Fpn, p));
        }

        return root;
    }

    /**
     * Returns the polynomial representing the value of this
     * <i>ExtensionFieldElement</i>.
     *
     * @return polynomial, the Polynomial representing the value of this
     *         <i>ExtensionFieldElement</i>.
     */
    public Polynomial getPolynomial() {
        return polynomial;
    }

    @Override
    public String toString() {
        /*
         * String content = ""; if(isInitialized()) { content =
         * polynomial.toString(); } return content;
         */
        return polynomial.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ExtensionFieldElement)) {
            return false;
        }

        ExtensionFieldElement g = (ExtensionFieldElement) obj;

        /*
         * if(!g.isInitialized() && !isInitialized()) { return true; }
         * if(!g.isInitialized() || !isInitialized()) { return false; }
         */

        return (this == obj)
                || (Fpn.equals(g.Fpn) && polynomial.equals(g.polynomial));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(Fpn);
        hash = 97 * hash + Objects.hashCode(polynomial);
        return hash;
    }

}
