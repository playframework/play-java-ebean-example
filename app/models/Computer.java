package models;

import java.util.*;
import javax.persistence.*;

import io.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import io.ebean.*;

/**
 * Computer entity managed by Ebean
 */
@Entity 
public class Computer extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date introduced;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date discontinued;
    
    @ManyToOne
    public Company company;

    /**
     * Return a paged list of computer
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Computer property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static PagedList<Computer> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
            Ebean.find(Computer.class).where()
                .ilike("name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("company")
                .setFirstRow(page * pageSize)
                .setMaxRows(pageSize)
                .findPagedList();
    }
    
}

