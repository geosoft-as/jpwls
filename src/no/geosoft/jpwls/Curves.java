package no.geosoft.jpwls;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Model all the curves defined by the PWLS standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Curves
{
  /** All curves defined by the PWLS standard. */
  private final Set<Curve> curves_ = new HashSet<>();

  /** Curves by mnemonic/company code. Cached for performance. */
  private final Map<String,Curve> curvesByMnemonic_ = new HashMap<>();

  /**
   * Create an empty curves instance.
   */
  public Curves()
  {
    // Nothing
  }

  /**
   * Add the specified curve to this instance.
   *
   * @param curve  Curve to add. Non-null.
   * @throws IllegalArgumentException  If curve is null.
   */
  public void add(Curve curve)
  {
    if (curve == null)
      throw new IllegalArgumentException("curve cannot be null");

    curves_.add(curve);

    String key = curve.getMnemonic() + curve.getCompanyCode();
    curvesByMnemonic_.put(key, curve);
  }

  /**
   * Return all the curves of this instance.
   *
   * @return  All the curves of this instance. Never null.
   */
  public Set<Curve> getAll()
  {
    return Collections.unmodifiableSet(curves_);
  }

  /**
   * Find the curve of the specified mnemonic/company code.
   *
   * @param mnemonic     Mnemonic of curve to find. Non.null.
   * @param companyCode  Company code of curve to find.
   * @return             Requested curve, or null if not found.
   * @throws IllegalArgumentException  If mnemonic is null.
   */
  public Curve find(String mnemonic, int companyCode)
  {
    if (mnemonic == null)
      throw new IllegalArgumentException("mnemonic cannot be null");

    String key = mnemonic + companyCode;
    return curvesByMnemonic_.get(key);
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (Curve curve : curves_) {
      s.append(curve.toString() + "\n");
    }

    return s.toString();
  }
}
