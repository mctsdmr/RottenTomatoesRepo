package BackEnd;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by erkanerol on 8/5/14.
 */
public class Container  implements Serializable{
    public ArrayList<Movie> movies;

    private static final long serialVersionUID = -1756553811677527910L;

    public Container(ArrayList<Movie> movies){
        this.movies=movies;
    }
}
