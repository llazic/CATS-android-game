package rs.etf.pmu.cats.viewModels;

import java.util.List;

import rs.etf.pmu.cats.customImageViews.ViewHolderCustomImageView;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

public class Inventory {
    public List<DrawableComponent> components;

    public List<DrawableComponent> currentlyInUse;

    public int draggedIndex = -1;

    public ViewHolderCustomImageView viewHolderCustomImageView;

    public DrawableComponent draggedDrawableComponent;
 }
