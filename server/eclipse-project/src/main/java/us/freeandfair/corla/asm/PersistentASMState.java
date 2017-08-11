/*
 * Free & Fair Colorado RLA System
 * 
 * @title ColoradoRLA
 * @created Aug 11, 2017
 * @copyright 2017 Free & Fair
 * @license GNU General Public License 3.0
 * @author Daniel M. Zimmerman <dmz@galois.com>
 * @description A system to assist in conducting statewide risk-limiting audits.
 */

package us.freeandfair.corla.asm;

import static us.freeandfair.corla.util.EqualsHashcodeHelper.nullableEquals;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import us.freeandfair.corla.Main;
import us.freeandfair.corla.persistence.AbstractEntity;

/**
 * An abstract state machine state, and sufficient information to reconstruct the
 * state machine it belongs to.
 * 
 * @author Daniel M. Zimmerman
 * @version 0.0.1
 */
@Entity
@Table(name = "asm_state")
// this class has many fields that would normally be declared final, but
// cannot be for compatibility with Hibernate and JPA.
@SuppressWarnings("PMD.ImmutableField")
public class PersistentASMState extends AbstractEntity implements Serializable {
  /**
   * The serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The class of AbstractStateMachine to which this state belongs, as a String.
   */
  @Column(updatable = false, nullable = false)
  private String my_asm_class;
  
  /**
   * The identifying information for the state machine, if any, as a String.
   */
  @Column(updatable = false)
  private String my_asm_identity;
  
  /**
   * The ASMState class (enum) containing this state, as a String.
   */
  private String my_state_class;
  
  /**
   * The state value, as a String.
   */
  private String my_state_value;
  
  /**
   * Constructs an empty PersistentASMState, solely for persistence.
   */
  protected PersistentASMState() {
    super();
  }
  
  /**
   * Constructs a PersistentASMState with the specified parameters.
   */
  protected PersistentASMState(final String the_asm_class,
                               final String the_asm_identity,
                               final String the_state_class,
                               final String the_state_value) {
    super();
    my_asm_class = the_asm_class;
    my_asm_identity = the_asm_identity;
    my_state_class = the_state_class;
    my_state_value = the_state_value;
  }
  
  /**
   * Obtains a PersistentASMState from an abstract state machine.
   * 
   * @param the_asm The ASM from which to obtain the state.
   * @return The state.
   */
  //@ requires the_asm != null;
  public static PersistentASMState stateFor(final AbstractStateMachine the_asm) {
    final String asm_class = the_asm.getClass().getName();
    // identifying info to be dealt with later
    final ASMState state = the_asm.currentState();
    final String state_class = state.getClass().getName();
    String state_value = null;
    if (state instanceof Enum<?>) {
      final Enum<?> state_enum = (Enum<?>) state;
      state_value = state_enum.name();
    }
    return new PersistentASMState(asm_class, null, state_class, state_value);
  }
  
  /**
   * Obtains an abstract state machine from a PersistentASMState.
   * 
   * @param the_state The state.
   * @return the state machine.
   * @exception IllegalArgumentException if the state machine cannot be 
   * constructed because the PersistentASMState contains invalid information.
   */
  //@ requires the_state != null;
  public static AbstractStateMachine asmFor(final PersistentASMState the_state) {
    try {
      // first, construct an ASM of the correct class
      final AbstractStateMachine result = 
          (AbstractStateMachine) Class.forName(the_state.asmClass()).newInstance();
      result.setIdentity(the_state.asmIdentity());
      // next, construct the class for the ASM state
      final Class<?> state_class = Class.forName(the_state.stateClass());
      if (state_class.isEnum() && ASMState.class.isAssignableFrom(state_class)) {
        // see if it has the right enum value
        for (final Object o : state_class.getEnumConstants()) {
          final Enum<?> enum_constant = (Enum<?>) o;
          if (enum_constant.name().equals(the_state.stateValue())) {
            result.setCurrentState((ASMState) enum_constant);
            break;
          }
        }
      }
      if (result.currentState() == null) {
        throw new InstantiationException("unable to find state value");
      }
      Main.LOGGER.info("constructed ASM for state " + the_state);
      return result;
    } catch (final ClassNotFoundException | IllegalAccessException | 
                   InstantiationException e) {
      Main.LOGGER.error("could not instantiate ASM for state " + the_state);
      throw new IllegalArgumentException(e);
    }
  }
  
  /**
   * @return the ASM class.
   */
  public String asmClass() {
    return my_asm_class;
  }
  
  /**
   * @return the ASM identity.
   */
  public String asmIdentity() {
    return my_asm_identity;
  }
  
  /**
   * @return the state class.
   */
  public String stateClass() {
    return my_state_class;
  }
  
  /**
   * @return the state value.
   */
  public String stateValue() {
    return my_state_value;
  }
  
  /**
   * @return a String representation of this ASM state.
   */
  @Override
  public String toString() {
    return "PersistentASMState [asm_class=" + my_asm_class + 
           ", asm_identity=" + my_asm_identity + 
           ", state_class=" + my_state_class + 
           ", state_value=" + my_state_value + "]";
  }

  /**
   * Compare this object with another for equivalence.
   * 
   * @param the_other The other object.
   * @return true if the objects are equivalent, false otherwise.
   */
  @Override
  public boolean equals(final Object the_other) {
    boolean result = true;
    if (the_other instanceof PersistentASMState) {
      final PersistentASMState other_state = (PersistentASMState) the_other;
      result &= nullableEquals(other_state.asmClass(), asmClass());
      result &= nullableEquals(other_state.asmIdentity(), asmIdentity());
      result &= nullableEquals(other_state.stateClass(), stateClass());
      result &= nullableEquals(other_state.stateValue(), stateValue());
    } else {
      result = false;
    }
    return result;
  }
  
  /**
   * @return a hash code for this object.
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
