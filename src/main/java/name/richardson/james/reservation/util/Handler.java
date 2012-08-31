
package name.richardson.james.reservation.util;

abstract public class Handler {

  protected static final Logger logger = new Logger(Handler.class);
  protected final String owner;

  public Handler(Class<?> parentClass) {
    this.owner = parentClass.getName();
    logger.debug("New handler created on behalf of " + owner);
  }

}
