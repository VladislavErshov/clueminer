package org.clueminer.clustering.gui.dlg;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import org.clueminer.chameleon.Chameleon;
import org.clueminer.clustering.api.AgglParams;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.factory.InternalEvaluatorFactory;
import org.clueminer.clustering.gui.ClusterAnalysis;
import org.clueminer.clustering.gui.ClusteringDialog;
import org.clueminer.distance.api.DistanceFactory;
import org.clueminer.partitioning.api.Bisection;
import org.clueminer.partitioning.api.BisectionFactory;
import org.clueminer.partitioning.impl.FiducciaMattheyses;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomas Bruna
 */
@ServiceProvider(service = ClusteringDialog.class)
public class ChameleonDialog extends ClusteringDialog {

    public ChameleonDialog() {
        initComponents();
        comboDistance.setSelectedItem("Euclidean");
        comboBisection.setSelectedItem("Fiduccia-Mattheyses");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        lbDistance = new javax.swing.JLabel();
        comboDistance = new javax.swing.JComboBox();
        lbCutoff = new javax.swing.JLabel();
        comboCutoff = new javax.swing.JComboBox();
        lbK = new javax.swing.JLabel();
        lbMaxPartitionSize = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tfK = new javax.swing.JTextField();
        chkBoxAutoK = new javax.swing.JCheckBox();
        chkBoxAutoMaxPSize = new javax.swing.JCheckBox();
        sliderK = new javax.swing.JSlider();
        sliderMaxPSize = new javax.swing.JSlider();
        tfMaxPSize = new javax.swing.JTextField();
        lbMaxPartitionSize1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        rbtnStandard = new javax.swing.JRadioButton();
        rbtnImproved = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        comboBisection = new javax.swing.JComboBox();
        labelLimit = new javax.swing.JLabel();
        sliderLimit = new javax.swing.JSlider();
        tfPriority = new javax.swing.JTextField();
        tfLimit = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(lbDistance, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbDistance.text")); // NOI18N
        lbDistance.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbDistance.toolTipText")); // NOI18N

        comboDistance.setModel(new DefaultComboBoxModel(initDistance()));
        comboDistance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDistanceActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lbCutoff, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbCutoff.text")); // NOI18N
        lbCutoff.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbCutoff.toolTipText")); // NOI18N

        comboCutoff.setModel(new DefaultComboBoxModel(initCutoff()));

        org.openide.awt.Mnemonics.setLocalizedText(lbK, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbK.text")); // NOI18N
        lbK.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbK.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbMaxPartitionSize, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize.text")); // NOI18N
        lbMaxPartitionSize.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize.toolTipText")); // NOI18N

        tfK.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfK.text")); // NOI18N
        tfK.setEnabled(false);
        tfK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfKActionPerformed(evt);
            }
        });

        chkBoxAutoK.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(chkBoxAutoK, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoK.text")); // NOI18N
        chkBoxAutoK.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoK.toolTipText")); // NOI18N
        chkBoxAutoK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxAutoKActionPerformed(evt);
            }
        });

        chkBoxAutoMaxPSize.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(chkBoxAutoMaxPSize, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoMaxPSize.text")); // NOI18N
        chkBoxAutoMaxPSize.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoMaxPSize.toolTipText")); // NOI18N
        chkBoxAutoMaxPSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxAutoMaxPSizeActionPerformed(evt);
            }
        });

        sliderK.setMaximum(500);
        sliderK.setMinimum(1);
        sliderK.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.sliderK.toolTipText")); // NOI18N
        sliderK.setValue(10);
        sliderK.setEnabled(false);
        sliderK.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderKStateChanged(evt);
            }
        });

        sliderMaxPSize.setMaximum(1000);
        sliderMaxPSize.setMinimum(1);
        sliderMaxPSize.setValue(10);
        sliderMaxPSize.setEnabled(false);
        sliderMaxPSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderMaxPSizeStateChanged(evt);
            }
        });

        tfMaxPSize.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfMaxPSize.text")); // NOI18N
        tfMaxPSize.setEnabled(false);
        tfMaxPSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfMaxPSizeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lbMaxPartitionSize1, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize1.text")); // NOI18N
        lbMaxPartitionSize1.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize1.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel1.toolTipText")); // NOI18N

        buttonGroup1.add(rbtnStandard);
        org.openide.awt.Mnemonics.setLocalizedText(rbtnStandard, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.rbtnStandard.text")); // NOI18N
        rbtnStandard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnStandardActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnImproved);
        rbtnImproved.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(rbtnImproved, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.rbtnImproved.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel2.text")); // NOI18N
        jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel2.toolTipText")); // NOI18N

        comboBisection.setModel(new DefaultComboBoxModel(initBisection()));
        comboBisection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBisectionActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(labelLimit, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.labelLimit.text")); // NOI18N
        labelLimit.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.labelLimit.toolTipText")); // NOI18N

        sliderLimit.setMinimum(1);
        sliderLimit.setValue(20);
        sliderLimit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderLimitStateChanged(evt);
            }
        });

        tfPriority.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfPriority.text")); // NOI18N
        tfPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPriorityActionPerformed(evt);
            }
        });

        tfLimit.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfLimit.text")); // NOI18N
        tfLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfLimitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(45, 45, 45)
                        .addComponent(rbtnStandard)
                        .addGap(39, 39, 39)
                        .addComponent(rbtnImproved))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbCutoff)
                        .addGap(64, 64, 64)
                        .addComponent(comboCutoff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(labelLimit))
                                .addGap(76, 76, 76)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(comboBisection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(sliderLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbDistance)
                                .addGap(43, 43, 43)
                                .addComponent(comboDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(41, 41, 41)
                        .addComponent(tfLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbK)
                            .addComponent(lbMaxPartitionSize)
                            .addComponent(lbMaxPartitionSize1))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sliderK, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sliderMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfK, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkBoxAutoK)
                                    .addComponent(chkBoxAutoMaxPSize))
                                .addGap(6, 6, 6)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(lbK)
                        .addGap(32, 32, 32)
                        .addComponent(lbMaxPartitionSize))
                    .addComponent(sliderK, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(sliderMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(tfK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(tfMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(chkBoxAutoK)
                        .addGap(28, 28, 28)
                        .addComponent(chkBoxAutoMaxPSize))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMaxPartitionSize1)
                    .addComponent(tfPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1))
                    .addComponent(rbtnStandard)
                    .addComponent(rbtnImproved))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(35, 35, 35)
                                .addComponent(labelLimit))
                            .addComponent(comboBisection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(sliderLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(lbDistance))
                            .addComponent(comboDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(lbCutoff))
                            .addComponent(comboCutoff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(tfLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboDistanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDistanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboDistanceActionPerformed

    private void tfKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfKActionPerformed
        int val = Integer.valueOf(tfK.getText());
        sliderK.setValue(val);
    }//GEN-LAST:event_tfKActionPerformed

    private void chkBoxAutoKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxAutoKActionPerformed
        if (chkBoxAutoK.isSelected()) {
            sliderK.setEnabled(false);
            tfK.setEnabled(false);
        } else {
            sliderK.setEnabled(true);
            tfK.setEnabled(true);
        }
    }//GEN-LAST:event_chkBoxAutoKActionPerformed

    private void tfMaxPSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfMaxPSizeActionPerformed
        int val = Integer.valueOf(tfMaxPSize.getText());
        sliderMaxPSize.setValue(val);
    }//GEN-LAST:event_tfMaxPSizeActionPerformed

    private void sliderKStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderKStateChanged
        tfK.setText(String.valueOf(sliderK.getValue()));
    }//GEN-LAST:event_sliderKStateChanged

    private void sliderMaxPSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderMaxPSizeStateChanged
        tfMaxPSize.setText(String.valueOf(sliderMaxPSize.getValue()));
    }//GEN-LAST:event_sliderMaxPSizeStateChanged

    private void chkBoxAutoMaxPSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxAutoMaxPSizeActionPerformed
        if (chkBoxAutoMaxPSize.isSelected()) {
            sliderMaxPSize.setEnabled(false);
            tfMaxPSize.setEnabled(false);
        } else {
            sliderMaxPSize.setEnabled(true);
            tfMaxPSize.setEnabled(true);
        }
    }//GEN-LAST:event_chkBoxAutoMaxPSizeActionPerformed

    private void rbtnStandardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnStandardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnStandardActionPerformed

    private void comboBisectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBisectionActionPerformed
        if ("Fiduccia-Mattheyses".equals((String) comboBisection.getSelectedItem())) {
            tfLimit.setVisible(true);
            sliderLimit.setVisible(true);
            labelLimit.setVisible(true);
        } else {
            tfLimit.setVisible(false);
            sliderLimit.setVisible(false);
            labelLimit.setVisible(false);
        }
    }//GEN-LAST:event_comboBisectionActionPerformed

    private void tfPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPriorityActionPerformed

    }//GEN-LAST:event_tfPriorityActionPerformed

    private void sliderLimitStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderLimitStateChanged
        tfLimit.setText(String.valueOf(sliderLimit.getValue()));
    }//GEN-LAST:event_sliderLimitStateChanged

    private void tfLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfLimitActionPerformed
        int val = Integer.valueOf(tfLimit.getText());
        sliderLimit.setValue(val);
    }//GEN-LAST:event_tfLimitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox chkBoxAutoK;
    private javax.swing.JCheckBox chkBoxAutoMaxPSize;
    private javax.swing.JComboBox comboBisection;
    private javax.swing.JComboBox comboCutoff;
    private javax.swing.JComboBox comboDistance;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelLimit;
    private javax.swing.JLabel lbCutoff;
    private javax.swing.JLabel lbDistance;
    private javax.swing.JLabel lbK;
    private javax.swing.JLabel lbMaxPartitionSize;
    private javax.swing.JLabel lbMaxPartitionSize1;
    private javax.swing.JRadioButton rbtnImproved;
    private javax.swing.JRadioButton rbtnStandard;
    private javax.swing.JSlider sliderK;
    private javax.swing.JSlider sliderLimit;
    private javax.swing.JSlider sliderMaxPSize;
    private javax.swing.JTextField tfK;
    private javax.swing.JTextField tfLimit;
    private javax.swing.JTextField tfMaxPSize;
    private javax.swing.JTextField tfPriority;
    // End of variables declaration//GEN-END:variables

    private Object[] initDistance() {
        return DistanceFactory.getInstance().getProvidersArray();
    }

    private Object[] initBisection() {
        return BisectionFactory.getInstance().getProvidersArray();
    }

    @Override
    public String getName() {
        return "Chameleon dialog";
    }

    @Override
    public Props getParams() {
        Props params = new Props();
        params.put(AgglParams.DIST, (String) comboDistance.getSelectedItem());
        params.put(AgglParams.CUTOFF_SCORE, (String) comboCutoff.getSelectedItem());
        params.putBoolean(AgglParams.CLUSTER_COLUMNS, false);
        return params;
    }

    @Override
    public void setParent(ClusterAnalysis clust) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClusteringAlgorithm getAlgorithm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public boolean isUIfor(ClusteringAlgorithm algorithm) {
        return "Chameleon".equals(algorithm.getName());
    }

    private Object[] initCutoff() {
        return InternalEvaluatorFactory.getInstance().getProvidersArray();
    }

    @Override
    public void updateAlgorithm(ClusteringAlgorithm algorithm) {
        if (!"Chameleon".equals(algorithm.getName())) {
            throw new RuntimeException("Chameleon algorithm expected");
        }
        Chameleon ch = (Chameleon) algorithm;
        if (chkBoxAutoK.isSelected()) {
            ch.setK(-1);
        } else {
            ch.setK(sliderK.getValue());
        }
        if (chkBoxAutoMaxPSize.isSelected()) {
            ch.setMaxPartitionSize(-1);
        } else {
            ch.setMaxPartitionSize(sliderMaxPSize.getValue());
        }
        ch.setClosenessPriority(Double.valueOf(tfPriority.getText()));

        if (rbtnImproved.isSelected()) {
            ch.setImprovedMeasure();
        } else {
            ch.setStandardMeasure();
        }

        String name = (String) comboBisection.getSelectedItem();
        Bisection bisection = BisectionFactory.getInstance().getProvider(name);
        if ("Fiduccia-Mattheyses".equals(name)) {
            FiducciaMattheyses fm = (FiducciaMattheyses) bisection;
            fm.setIterationLimit(Integer.valueOf(tfLimit.getText()));
        }
        ch.setBisection(bisection);
    }
}
