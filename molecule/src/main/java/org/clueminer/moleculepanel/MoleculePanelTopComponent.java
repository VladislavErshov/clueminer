package org.clueminer.moleculepanel;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.clueminer.hts.api.HtsInstance;
import org.clueminer.hts.api.HtsPlate;
import org.clueminer.project.api.Project;
import org.clueminer.project.api.ProjectController;
import org.clueminer.project.api.Workspace;
import org.clueminer.project.api.WorkspaceListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/**
 * Top component which displays structure of molecules.
 */
@ConvertAsProperties(
        dtd = "-//org.clueminer.moleculepanel//MoleculePanel//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "MoleculePanelTopComponent",
        iconBase = "org/clueminer/moleculepanel/hexane16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "org.clueminer.moleculepanel.MoleculePanelTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MoleculePanelAction",
        preferredID = "MoleculePanelTopComponent")
@Messages({
    "CTL_MoleculePanelAction=MoleculePanel",
    "CTL_MoleculePanelTopComponent=MoleculePanel Window",
    "HINT_MoleculePanelTopComponent=This is a MoleculePanel window"
})
public final class MoleculePanelTopComponent extends TopComponent implements LookupListener {

    private static final long serialVersionUID = -2559104276598823748L;
    private final InstanceContent content = new InstanceContent();
    private MoleculesGroup panel;
    private Lookup.Result<HtsPlate> htsResult = null;
    private static final Logger logger = Logger.getLogger(MoleculePanelTopComponent.class.getName());
    private static Project project;

    public MoleculePanelTopComponent() {
        //Add the dynamic object to the TopComponent Lookup:
        associateLookup(new AbstractLookup(content));
        initComponents();
        setName(Bundle.CTL_MoleculePanelTopComponent());
        setToolTipText(Bundle.HINT_MoleculePanelTopComponent());
        panel = new MoleculesGroup();
        add(panel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {


        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.addWorkspaceListener(new WorkspaceListener() {
            @Override
            public void initialize(Workspace workspace) {
                logger.log(Level.INFO, "molecule panel listener initialized");
            }

            @Override
            public void select(Workspace workspace) {
                System.out.println("workspace: " + workspace.toString());
                logger.log(Level.INFO, "selected");
                System.out.println("workspace selected: got result (plate)");
                htsResult = workspace.getLookup().lookupResult(HtsPlate.class);
                System.out.println("lookup res= " + htsResult.toString());

                HtsPlate plt = workspace.getLookup().lookup(HtsPlate.class);
                System.out.println("got plate, size: " + plt);
                panel.plateUpdate(plt);
            }

            @Override
            public void unselect(Workspace workspace) {
                logger.log(Level.INFO, "component unselected");
            }

            @Override
            public void close(Workspace workspace) {
                logger.log(Level.INFO, "component closed");
            }

            @Override
            public void disable() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void projectActivated(Project proj) {
                project = proj;
                projectChanged();
            }
        });
        //this should display content from InstanceContent
        htsResult = Utilities.actionsGlobalContext().lookupResult(HtsPlate.class);
        htsResult.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        htsResult.removeLookupListener(this);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        System.out.println("molecule event :" + ev.toString());

        if (htsResult != null) {
            System.out.println("molecule panel: got HTS Plate");
            Collection<? extends HtsPlate> allPlates = htsResult.allInstances();
            for (HtsPlate<HtsInstance> d : allPlates) {
                panel.plateUpdate(d);
            }
        }
    }

    protected void projectChanged() {
        final HtsPlate plt = project.getLookup().lookup(HtsPlate.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.plateUpdate(plt);
            }
        });

    }
}
