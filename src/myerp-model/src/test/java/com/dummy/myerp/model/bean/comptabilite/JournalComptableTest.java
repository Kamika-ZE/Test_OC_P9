package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JournalComptableTest {

    private JournalComptable journalComptable;

    @Before
    public void initJournalComptable(){
        journalComptable = new JournalComptable();
        journalComptable.setCode("AC");
        journalComptable.setLibelle("Achat");
    }

    @Test
    public void testJournalComptableToString(){
        Assert.assertEquals("JournalComptable{code='AC', libelle='Achat'}", journalComptable.toString());
    }
}
