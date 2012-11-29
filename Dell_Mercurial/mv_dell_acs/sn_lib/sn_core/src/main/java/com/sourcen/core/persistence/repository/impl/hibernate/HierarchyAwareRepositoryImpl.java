/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.persistence.domain.HierarchyAwareEntity;
import com.sourcen.core.persistence.repository.HierarchyAwareRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;


/**
 * A repository subclass that provides tree based implementations. All objects the repository handles must be of type
 * {@link javax.swing.tree.TreeNode}
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
public abstract class HierarchyAwareRepositoryImpl<R extends HierarchyAwareEntity> extends IdentifiableEntityRepositoryImpl<Long, R> implements HierarchyAwareRepository<R> {

    public HierarchyAwareRepositoryImpl(final Class<?> entityClass) {
        super(entityClass);

    }

    /**
     * this is a two set process,insert using stored procedure, then merge the values.
     */
    @Override
    @Transactional
    public void insert(final R record) {
        final Session session = getSession();
        final Query query = session.createSQLQuery("call treeNodes_insert(?,?,?)");
        query.setString(0, this.entityTableName);
        query.setParameter(1, record.getId());
        query.setParameter(2, record.getParentId());
        // the result is the last inserted-id from the database.
        final BigInteger insertedId = (BigInteger) query.uniqueResult();

        final R dbRecord = super.get(insertedId.longValue());
        record.setId(dbRecord.getId());
        final Long lft = dbRecord.getLeft();
        final Long rgt = dbRecord.getRight();
        record.setVersion(dbRecord.getVersion());
        final R mergedObject = (R) session.merge(record);
        mergedObject.setLeft(lft);
        mergedObject.setDepth(record.getDepth());
        mergedObject.setRight(rgt);
        session.update(mergedObject);
        onInsert(record);
    }

    @Override
    @Transactional
    public void update(final R record) {
        final R dbRecord = get(record.getId());
        // check if any of the core hierarchy properties were changed.
        if (!dbRecord.getLeft().equals(record.getLeft()) || !dbRecord.getRight().equals(record.getRight()) || !dbRecord.getParentId().equals(record.getParentId())
                || !dbRecord.getDepth().equals(record.getDepth())) {
            throw new UnsupportedOperationException("HierarchyAwareRepository#update will only not update "
                    + "left, rgt, parentId and depth properties. Please use HierarchyAwareRepository#move method "
                    + "if you want to change the position of a item in the Hierarchy.");
        }
        super.update(record);
    }

    ;

    @Override
    public void put(final R record) {
        if (record.getId() != null) {
            update(record);
        } else {
            insert(record);
        }
    }

    ;

    @Override
    public void remove(final Long entityId) {
        final R record = get(entityId);
        if (record != null) {
            remove(record);
        }
    }

    @Override
    @Transactional
    public void remove(final R record) {

        // -- SELECT @myLeft := lft, @myRight := rgt, @myWidth := rgt - lft + 1 FROM permissions_groups WHERE id = ?;
        // SET @SELECT_VARS_QUERY = CONCAT('SELECT @myLeft := lft, @myRight := rgt, @myWidth := rgt - lft + 1 FROM
        // ',tableName,' WHERE id = ?');
        // PREPARE SELECT_VARS_QUERY_STMT FROM @SELECT_VARS_QUERY;
        // EXECUTE SELECT_VARS_QUERY_STMT USING @objectId;
        //
        // -- lets check if the record exists first.
        // IF @myLeft IS NOT NULL AND @myRight IS NOT NULL AND @myWidth>0 THEN
        //
        // -- DELETE FROM permissions_groups WHERE lft BETWEEN @myLeft AND @myRight;
        // SET @DELETE_OBJ_QUERY = CONCAT('DELETE FROM ', tableName ,' WHERE lft BETWEEN @myLeft AND @myRight;');
        // PREPARE DELETE_QUERY_STMT FROM @DELETE_OBJ_QUERY;
        // EXECUTE DELETE_QUERY_STMT;
        // DEALLOCATE PREPARE DELETE_QUERY_STMT;
        //
        // -- UPDATE permissions_groups SET rgt = rgt - @myWidth WHERE rgt > @myRight;
        // SET @UPDATE_RGT_QUERY = CONCAT('UPDATE ', tableName ,' SET rgt = rgt - @myWidth WHERE rgt > @myRight');
        // PREPARE UPDATE_RGT_QUERY_STMT FROM @UPDATE_RGT_QUERY;
        // EXECUTE UPDATE_RGT_QUERY_STMT;
        // DEALLOCATE PREPARE UPDATE_RGT_QUERY_STMT;
        //
        // -- UPDATE permissions_groups SET lft = lft - @myWidth WHERE lft > @myRight;
        // SET @UPDATE_LFT_QUERY = CONCAT('UPDATE ', tableName ,' SET lft = lft - @myWidth WHERE lft > @myRight');
        // PREPARE UPDATE_LFT_QUERY_STMT FROM @UPDATE_LFT_QUERY;
        // EXECUTE UPDATE_LFT_QUERY_STMT;
        // DEALLOCATE PREPARE UPDATE_LFT_QUERY_STMT;

        // -- SELECT @myLeft := lft, @myRight := rgt, @myWidth := rgt - lft + 1 FROM ',tableName,' WHERE id = ?
        final Long myLeft = record.getLeft();
        final Long myRight = record.getRight();
        final Long myWidth = myRight - myLeft + 1;

        if (myLeft > -1 && myRight > -1 && myWidth > 0) {
            final Session session = getSession();

            // Query query = session.createSQLQuery("call treeNodes_delete(?,?)");

            // query.setString(0, this.ENTITY_TABLENAME);
            // query.setParameter(1, record.getId());
            // the result is the last inserted-id from the database.
            // Object result = query.uniqueResult();

            // -- DELETE FROM table WHERE lft BETWEEN @myLeft AND @myRight;
            // .setParameter("myLeft", myLeft).setParameter("myRight", myRight).executeUpdate();
            // doing it the hibernate collections way since we want to remove all references as well.
            // the Object needs to be wired, so that Hibernate delete's the sub items of the parent.
            super.remove(record);

            // -- UPDATE table SET rgt = rgt - @myWidth WHERE rgt > @myRight;
            session.createQuery("UPDATE " + this.entityClassName + " e SET e.right = e.right-:myWidth WHERE e.right > :myRight").setParameter("myWidth", myWidth)
                    .setParameter("myRight", myRight).executeUpdate();

            // -- UPDATE table SET lft = lft - @myWidth WHERE lft > @myRight;
            session.createQuery("UPDATE " + this.entityClassName + " e SET e.left = e.left-:myWidth WHERE e.left > :myRight").setParameter("myWidth", myWidth)
                    .setParameter("myRight", myRight).executeUpdate();
        } else {
            throw new UnsupportedOperationException("cannot delete a HierarchicalEntity without left, and right values :record::" + record);
        }

    }

    ;

    private final String getTree_HQL = "FROM " + this.entityClassName + " as node, " + this.entityClassName
            + " as parent WHERE parent.id = ? and (node.left BETWEEN parent.left AND parent.right)";

    @Override
    public List<R> getTree(final Long recordId) {
        // SELECT n.* from system_permissions as n, system_permissions as n2
        // WHERE n2.id =3 AND n.lft BETWEEN n2.lft AND n2.rgt;

        final Query query = getSession().createQuery(this.getTree_HQL);
        query.setLong(0, recordId);
        return onFindForList(query.list());
    }

    private final String getPath_HQL = "FROM " + this.entityClassName + " WHERE lft < ? AND rgt > ? ORDER BY lft ASC";

    @Override
    public List<R> getPath(final R record) {
        final Query query = getSession().createQuery(this.getPath_HQL);
        query.setLong(0, record.getLeft());
        query.setLong(1, record.getRight());
        return onFindForList(query.list());
    }

    private final String getChildren_HQL = "FROM " + this.entityClassName + " as node, " + this.entityClassName + " as parent WHERE "
            + "node.left BETWEEN parent.left AND parent.right " + "AND parent.id=? GROUP BY node.id ORDER BY node.left";

    @Override
    public List<R> getChildren(final Long recordId) {
        final Session session = getSession();
        return onFindForList(session.createQuery(this.getChildren_HQL).setParameter(0, recordId).list());
    }

    @Override
    public void moveTo(final R record, final R newParent) {
        throw new UnsupportedOperationException();
    }

    ;
}
