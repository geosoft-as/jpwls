package no.geosoft.pwls;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Curves
{
  private final Set<Curve> curves_ = new HashSet<>();

  private final Map<String,Curve> curvesByMnemonic_ = new HashMap<>();

  public Curves()
  {
  }

  public void add(Curve curve)
  {
    if (curve == null)
      throw new IllegalArgumentException("curve cannot be null");

    curves_.add(curve);

    String key = curve.getMnemonic() + curve.getCompanyCode();
    curvesByMnemonic_.put(key, curve);
  }

  public Set<Curve> getAll()
  {
    return Collections.unmodifiableSet(curves_);
  }

  public Curve get(String mnemonic, int companyCode)
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
