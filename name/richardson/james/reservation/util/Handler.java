package name.richardson.james.reservation.util;


abstract public class Handler {

  protected final Logger logger = new Logger(this.getClass());
  protected final String owner;
  
  public Handler(Class<?> parentClass) {
    this.owner = parentClass.getName();
    logger.debug("New handler created on behalf of " + owner);
  }
  
}
