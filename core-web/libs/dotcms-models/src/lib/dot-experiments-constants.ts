import { GOAL_OPERATORS, GOAL_PARAMETERS, GOAL_TYPES, Goals } from './dot-experiments.model';
import { DotDropdownSelectOption } from './shared-models';

export const MAX_VARIANTS_ALLOWED = 3;

export const DEFAULT_VARIANT_ID = 'DEFAULT';

export const DEFAULT_VARIANT_NAME = 'Original';

export const SESSION_STORAGE_VARIATION_KEY = 'variantName';

export const TIME_14_DAYS = 12096e5;

export const TIME_90_DAYS = 7776e6;

export const PROP_NOT_FOUND = 'NOT_FOUND';

export enum ExperimentsConfigProperties {
    EXPERIMENTS_MIN_DURATION = 'EXPERIMENTS_MIN_DURATION',
    EXPERIMENTS_MAX_DURATION = 'EXPERIMENTS_MAX_DURATION'
}

export enum TrafficProportionTypes {
    SPLIT_EVENLY = 'SPLIT_EVENLY',
    CUSTOM_PERCENTAGES = 'CUSTOM_PERCENTAGES'
}

export const MAX_INPUT_TITLE_LENGTH = 50;

export const MAX_INPUT_DESCRIPTIVE_LENGTH = 255;

// Keep the order of this enum is important to respect the order of the experiment listing.
export enum DotExperimentStatusList {
    RUNNING = 'RUNNING',
    SCHEDULED = 'SCHEDULED',
    DRAFT = 'DRAFT',
    ENDED = 'ENDED',
    ARCHIVED = 'ARCHIVED'
}

export const ExperimentsStatusList: Array<DotDropdownSelectOption<string>> = [
    {
        label: 'draft',
        value: DotExperimentStatusList.DRAFT
    },
    {
        label: 'running',
        value: DotExperimentStatusList.RUNNING
    },
    {
        label: 'ended',
        value: DotExperimentStatusList.ENDED
    },
    {
        label: 'archived',
        value: DotExperimentStatusList.ARCHIVED
    },
    {
        label: 'scheduled',
        value: DotExperimentStatusList.SCHEDULED
    }
];

export const GoalsConditionsParametersListByType: Partial<
    Record<GOAL_TYPES, Array<DotDropdownSelectOption<GOAL_PARAMETERS>>>
> = {
    [GOAL_TYPES.URL_PARAMETER]: [
        {
            label: 'experiments.goal.conditions.params.query_param.label',
            value: GOAL_PARAMETERS.QUERY_PARAM,
            inactive: false
        }
    ],
    [GOAL_TYPES.REACH_PAGE]: [
        {
            label: 'experiments.goal.conditions.params.url.label',
            value: GOAL_PARAMETERS.URL,
            inactive: false
        }
    ]
};

type SelectOptionsOperators = Array<DotDropdownSelectOption<GOAL_OPERATORS>>;
export const GoalsConditionsOperatorsListByType: Partial<
    Record<GOAL_TYPES, SelectOptionsOperators>
> = {
    [GOAL_TYPES.URL_PARAMETER]: [
        {
            label: 'experiments.goal.conditions.operators.contains.label',
            value: GOAL_OPERATORS.CONTAINS,
            inactive: false
        },
        {
            label: 'experiments.goal.conditions.operators.equals.label',
            value: GOAL_OPERATORS.EQUALS,
            inactive: false
        },
        {
            label: 'experiments.goal.conditions.operators.exists.label',
            value: GOAL_OPERATORS.EXISTS,
            inactive: false
        }
    ],
    [GOAL_TYPES.REACH_PAGE]: [
        {
            label: 'experiments.goal.conditions.operators.contains.label',
            value: GOAL_OPERATORS.CONTAINS,
            inactive: false
        },
        {
            label: 'experiments.goal.conditions.operators.equals.label',
            value: GOAL_OPERATORS.EQUALS,
            inactive: false
        }
    ]
};

export enum SIDEBAR_STATUS {
    OPEN = 'OPEN',
    CLOSE = 'CLOSED'
}

export const DefaultGoalConfiguration: Goals = {
    primary: {
        name: 'default',
        type: GOAL_TYPES.REACH_PAGE,
        conditions: [
            {
                parameter: GOAL_PARAMETERS.URL,
                operator: GOAL_OPERATORS.EQUALS,
                value: 'to-define'
            }
        ]
    }
};

export const GOALS_METADATA_MAP: Record<GOAL_TYPES, { label: string; description: string }> = {
    [GOAL_TYPES.REACH_PAGE]: {
        label: 'experiments.goal.reach_page.name',
        description: 'experiments.goal.reach_page.description'
    },
    [GOAL_TYPES.BOUNCE_RATE]: {
        label: 'experiments.goal.bounce_rate.name',
        description: 'experiments.goal.bounce_rate.description'
    },
    [GOAL_TYPES.CLICK_ON_ELEMENT]: {
        label: 'experiments.goal.click_on_element.name',
        description: 'experiments.goal.click_on_element.description'
    },
    [GOAL_TYPES.URL_PARAMETER]: {
        label: 'experiments.goal.url_parameter.name',
        description: 'experiments.goal.url_parameter.description'
    }
};

export const daysOfTheWeek = [
    'Sunday',
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday'
];

export type SummaryLegend = { icon: string; legend: string };

export const enum BayesianStatusResponse {
    TIE = 'TIE',
    NONE = 'NONE'
}

export const BayesianNoWinnerStatus: Array<string> = [
    BayesianStatusResponse.NONE,
    BayesianStatusResponse.TIE
];

const enum BayesianLegendStatus {
    WINNER = 'WINNER',
    NO_WINNER_FOUND = 'NO_WINNER_FOUND',
    NO_WINNER_FOUND_YET = 'NO_WINNER_FOUND_YET',
    NO_ENOUGH_SESSIONS = 'NO_ENOUGH_SESSIONS',
    PRELIMINARY_WINNER = 'PRELIMINARY_WINNER'
}

export const ReportSummaryLegendByBayesianStatus: Record<BayesianLegendStatus, SummaryLegend> = {
    [BayesianLegendStatus.WINNER]: {
        icon: 'dot-trophy',
        legend: 'experiments.summary.suggested-winner.winner-is'
    },
    [BayesianLegendStatus.PRELIMINARY_WINNER]: {
        icon: 'dot-trophy',
        legend: 'experiments.summary.suggested-winner.preliminary-winner-is'
    },
    [BayesianLegendStatus.NO_WINNER_FOUND]: {
        icon: 'pi-ban',
        legend: 'experiments.summary.suggested-winner.no-winner-found'
    },
    [BayesianLegendStatus.NO_WINNER_FOUND_YET]: {
        icon: 'pi-ban',
        legend: 'experiments.summary.suggested-winner.no-winner-found-yet'
    },
    [BayesianLegendStatus.NO_ENOUGH_SESSIONS]: {
        icon: 'pi-ban',
        legend: 'experiments.summary.suggested-winner.no-enough-sessions'
    }
};
